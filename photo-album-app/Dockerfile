# syntax=docker/dockerfile:1.4

FROM --platform=$BUILDPLATFORM node:22.7.0-bullseye-slim as photo-album-app

RUN mkdir /project
WORKDIR /project

RUN npm install -g @angular/cli@18.2.3

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
CMD ["ng", "serve", "--host", "0.0.0.0"]

FROM photo-album-app as dev-envs

RUN <<EOF
apt-get update
apt-get install -y --no-install-recommends git
EOF

RUN <<EOF
useradd -s /bin/bash -m vscode
groupadd docker
usermod -aG docker vscode
EOF
# install Docker tools (cli, buildx, compose)
COPY --from=gloursdocker/docker / /

CMD ["ng", "serve", "--host", "0.0.0.0"]
