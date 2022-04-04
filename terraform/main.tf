terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "3.88.0"
    }
    helm = {
      source = "hashicorp/helm"
      version = "2.5.0"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
  zone    = var.zone
}

provider "helm" {
  kubernetes {
    token                  = data.google_client_config.default.access_token
    host                   = resource.google_container_cluster.vpc_native_cluster.endpoint
    cluster_ca_certificate = base64decode(resource.google_container_cluster.vpc_native_cluster.master_auth[0].cluster_ca_certificate)
  }
}

data "google_client_config" "default" {}

resource "google_service_account" "default" {
  account_id   = "k8s-service-account-id"
  display_name = "K8s Service Account"
}

resource "google_project_iam_member" "registry_reader_binding" {
  role   = "roles/containerregistry.ServiceAgent"
  member = "serviceAccount:${google_service_account.default.email}"
}

resource "google_compute_network" "vpc" {
  name                    = "test-network"
  auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "vpc_subnet" {
  name          = "test-subnetwork"
  ip_cidr_range = "10.2.0.0/16"
  region        = var.region
  network       = google_compute_network.vpc.id

  secondary_ip_range {
    range_name    = "services-range"
    ip_cidr_range = "192.168.1.0/24"
  }

  secondary_ip_range {
    range_name    = "pod-ranges"
    ip_cidr_range = "192.168.64.0/22"
  }
}

resource "google_container_cluster" "vpc_native_cluster" {
  name                     = "my-gke-cluster"
  remove_default_node_pool = true
  initial_node_count       = 1

  network    = google_compute_network.vpc.id
  subnetwork = google_compute_subnetwork.vpc_subnet.id

  ip_allocation_policy {
    cluster_secondary_range_name  = google_compute_subnetwork.vpc_subnet.secondary_ip_range.0.range_name
    services_secondary_range_name = google_compute_subnetwork.vpc_subnet.secondary_ip_range.1.range_name
  }
}

resource "google_container_node_pool" "vpc_native_cluster_preemptible_nodes" {
  name       = "my-node-pool"
  cluster    = google_container_cluster.vpc_native_cluster.name
  node_count = 1

  node_config {
    preemptible  = true
    machine_type = "e2-medium"

    service_account = google_service_account.default.email
    oauth_scopes    = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }
}

resource "google_compute_global_address" "private_ip" {
  name          = "private-ip-address"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = google_compute_network.vpc.id
}

resource "google_service_networking_connection" "service_vpc_connection" {
  network                 = google_compute_network.vpc.id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip.name]
}

resource "helm_release" "consul" {
  name       = "consul"
  repository = "https://helm.releases.hashicorp.com"
  chart      = "hashicorp/consul-helm"
}
