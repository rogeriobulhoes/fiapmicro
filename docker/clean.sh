echo "removing containers"
docker rm docker_discovery_1
docker rm docker_zuul_1
docker rm docker_clientes_1
docker rm docker_filmes_1
docker rm docker_servicos_1

echo "removing images"
docker rmi docker_discovery
docker rmi docker_zuul
docker rmi docker_clientes
docker rmi docker_filmes
docker rmi docker_servicos
