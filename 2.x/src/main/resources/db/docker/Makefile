TAG = "Makefile"

DOCKER = docker
DOCKER_HOST_IP := $(shell ipconfig getifaddr en0)

##
## Compose
##

.PHONY: compose.prepare
compose.prepare:
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Preparing docker-compose"
	@ echo "-----------------------------------------\n"
	@ echo "export DOCKER_HOST_IP=$(DOCKER_HOST_IP)"
	@ echo "\n-----------------------------------------"
	@ echo ""

.PHONY: compose.local
compose.local: compose.prepare
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Running docker-compose"
	@ DOCKER_HOST_IP=$(DOCKER_HOST_IP) docker-compose -f docker-compose.local.yml -p my-sbbp-local-db-set up


.PHONY: compose.clean
compose.clean:
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Starting: Cleaning docker resources"
	@ echo "-----------------------------------------\n"
	@ docker-compose -f docker-compose.local.yml -p my-sbbp-local-db-set down || true
	@ docker image rm $(docker image ls -a --filter "dangling=true" -q) || true
	@ docker container rm $(docker container ls -a --filter "status=exited" -q) || true
	@ echo "\n-----------------------------------------"
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Finished: Cleaning docker resources"