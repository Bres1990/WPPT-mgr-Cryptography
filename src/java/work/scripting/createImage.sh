#!/usr/bin/env bash

build() {
  IMAGE_NAME = 'docker-image'

  docker build -t ${IMAGE_NAME} .

  [ $? != 0 ] && \
    error "Docker image build failed !" && exit 100
}