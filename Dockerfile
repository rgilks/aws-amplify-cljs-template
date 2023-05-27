FROM amazonlinux:2

ENV VERSION_NODE_DEFAULT=16
ENV VERSION_YARN=1.22.0
ENV VERSION_AMPLIFY=12.0.0

## Install OS packages
RUN touch ~/.bashrc
RUN yum -y update && \
    yum -y install \
        alsa-lib-devel \
        autoconf \
        automake \
        bzip2 \
        bison \
        bzr \
        cmake \
        expect \
        fontconfig \
        git \
        gcc-c++ \
        GConf2-devel \
        gtk2-devel \
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
        java-11-amazon-corretto-headless \
    yum clean all && \
    rm -rf /var/cache/yum

## Install Clojure
RUN curl -O https://download.clojure.org/install/linux-install-1.11.1.1252.sh
RUN chmod +x linux-install-1.11.1.1252.sh
RUN ./linux-install-1.11.1.1252.sh

## Install Chrome
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
RUN yum install -y ./google-chrome-stable_current_*.rpm

## Install Node
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
RUN /bin/bash -c ". ~/.nvm/nvm.sh && nvm install ${VERSION_NODE_DEFAULT} && nvm use ${VERSION_NODE_DEFAULT} && chown -R root:root /root/.nvm &&  \
	npm install -g yarn@${VERSION_YARN} && \
	nvm alias default ${VERSION_NODE_DEFAULT} && nvm cache clear"

# Handle yarn for any `nvm install` in the future
RUN echo "yarn@${VERSION_YARN}" > /root/.nvm/default-packages

ENV PATH /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

## Install AWS Amplify CLI
RUN /bin/bash -c ". ~/.nvm/nvm.sh && nvm use ${VERSION_NODE_DEFAULT} && \
    npm config set user 0 && npm config set unsafe-perm true && \
	npm install -g @aws-amplify/cli@${VERSION_AMPLIFY}"

## Installing Cypress
RUN /bin/bash -c ". ~/.nvm/nvm.sh && \
    nvm use ${VERSION_NODE_DEFAULT} && \
    npm install -g --unsafe-perm=true --allow-root cypress"

## Environment Setup
RUN echo export PATH="/root/.nvm/versions/node/${VERSION_NODE_DEFAULT}/bin:\
$PATH" >> ~/.bashrc && \
echo "nvm use ${VERSION_NODE_DEFAULT} 1> /dev/null" >> ~/.bashrc

ENTRYPOINT [ "bash", "-c" ]
