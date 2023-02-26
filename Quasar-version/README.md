# Forms (ubforms)

Forms with premium features

## Pre Requirements

### First step: Install NVM(Node Version Manager)

#### Install `nvm` using `curl` or `wget`(both can be installed with sudo apt-get install `curl`/`wget`) :
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
```
Or:
```bash
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
```

#### Then execute the following 2 command lines in order:
```bash
export NVM_DIR="$([ -z "${XDG_CONFIG_HOME-}" ] && printf %s "${HOME}/.nvm" || printf %s "${XDG_CONFIG_HOME}/nvm")"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" # This loads nvm
```

#### Verify installation
```bash
command -v nvm
```
This should output `nvm`

### Second step: Install Node JS and NPM(Node Package Manager)
```bash
nvm install 18.14.2
```
Similar to `nvm install --lts` since `18.14.2` is the latest version of the Node Hydrogen release

#### Verify npm and node versions:
```bash
npm --version
```
```bash
node -v
```

#### Set the installed Node version to default:
```bash
nvm alias default 18.14.2
```
This version will be automatically selected when a new session spawns. 
You can also reference it by the alias like in the following command:
```bash
nvm use default
```

### Last step: Install Quasar Framework
```bash
npm install -g @quasar/cli
```

## Install the dependencies
```bash
yarn
# or
npm install
```

## Start the app in development mode (hot-code reloading, error reporting, etc.)
```bash
quasar dev
```


## Lint the files
```bash
yarn lint
# or
npm run lint
```


### Format the files
```bash
yarn format
# or
npm run format
```



## Build the app for production
```bash
quasar build
```

## Customize the configuration
See [Configuring quasar.config.js](https://v2.quasar.dev/quasar-cli-webpack/quasar-config-js).
