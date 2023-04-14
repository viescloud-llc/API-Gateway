namespace=api-gateway-dev
cd k8s
microk8s kubectl delete namespace ${namespace}
microk8s kubectl create namespace ${namespace}
microk8s kubectl -n ${namespace} apply -f ${namespace}_deployment.yaml
microk8s kubectl -n ${namespace} apply -f ${namespace}_service.yaml