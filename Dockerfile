FROM amazonlinux:2023

ENV VERSION_NODE_DEFAULT=22
ENV VERSION_YARN=1.22.22
ENV VERSION_AMPLIFY=14.2.3
ENV VERSION_CYPRESS=16.2.1

## Install OS packages
RUN touch ~/.bashrc
RUN dnf -y update && \
    dnf -y install \
        alsa-lib-devel \
        autoconf \
        automake \
        bzip2 \
        bison \
        cmake \
        expect \
        fontconfig \
        git \
        gcc-c++ \
        gtk3-devel \
        libnotify-devel \
        libpng \
        libpng-devel \
        libffi-devel \
        libtool \
        libX11 \
        libXext \
        libxml2 \
        libxml2-devel \
        libXScrnSaver \
        libxslt \
        libxslt-devel \
        libyaml \
        libyaml-devel \
        make \
        nss-devel \
        openssl-devel \
        openssh-clients \
        patch \
        procps \
        python3 \
        python3-devel \
        readline-devel \
        sqlite-devel \
        tar \
        tree \
        unzip \
        wget \
        which \
        xorg-x11-server-Xvfb \
        zip \
        zlib \
        zlib-devel \
        java-17-amazon-corretto-headless && \
    dnf clean all && \
    rm -rf /var/cache/dnf

## Install Clojure
RUN curl -O https://download.clojure.org/install/linux-install-1.11.1.1252.sh
RUN chmod +x linux-install-1.11.1.1252.sh
RUN ./linux-install-1.11.1.1252.sh

## Install Chrome
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
RUN dnf install -y ./google-chrome-stable_current_*.rpm

## Install Node
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.4/install.sh | bash
RUN /bin/bash -c '. /root/.nvm/nvm.sh && \
    nvm install "$VERSION_NODE_DEFAULT" && \
    npm install -g "yarn@$VERSION_YARN" && \
    nvm alias default "$VERSION_NODE_DEFAULT" && \
    ln -s "$(dirname "$(dirname "$(command -v node)")")" /opt/node-current && \
    nvm cache clear'

# Handle yarn for any `nvm install` in the future
RUN echo "yarn@${VERSION_YARN}" > /root/.nvm/default-packages

ENV PATH="/opt/node-current/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"

## Install AWS Amplify CLI
RUN npm install -g "@aws-amplify/cli@${VERSION_AMPLIFY}"

## Installing Cypress
RUN npm install -g "cypress@${VERSION_CYPRESS}"

ENTRYPOINT [ "bash", "-c" ]
