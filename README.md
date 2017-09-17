![](https://travis-ci.org/sdelrio/jenkins.svg?branch=master)

Jenkins master docker installation with base plugins and no wizard

# Requirements

- Docker swarm (for docker stack deploy).
- Docker secrets:
  - `jenkins-user` (`echo "myuser" | docker secret create jenkins-user -`)
  - `jenkins-pass` (`echo "mypass" | docker secret create jenkins-pass -`)

# `jenkins-local.yml`

- Deploy stack
    ```
    docker stack deploy -c jenkins-local.yml jenkins
    ```
- Open browser to <http://localhost:8080>

# `jenkins-aws.yml`

ToDo


