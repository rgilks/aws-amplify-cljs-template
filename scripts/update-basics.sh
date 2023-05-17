# This script..
# updates nvm, node, brew and upgrades yarn
# updates any cli tools we are using

curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"
nvm install 16
nvm use node

brew update
brew upgrade yarn

npm i -g @aws-amplify/cli
npm i -g npm-check-updates
