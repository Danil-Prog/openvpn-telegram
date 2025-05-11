#!/bin/bash

r='\033[0;31m' # red
g='\033[0;32m' # green
y='\033[0;33m' # yellow
nc='\033[0m'   # No Color

HELP="Available parameters:

  --help(-h)        :get list commands.
  --start(-s)       :start program local
  --stop            :stop program
  --check-upgrade   :checks if there are any updates available
  --upgrade         :upgrade application
"

ARG=$1
LOG_FILE_PATH='openvpn-telegram.log'
ENV_PATH='.env'

#-------Logger

function log() {
  dateFormat  "${nc}" "[LOG]: ${1}${nc}"
}

function info() {
  dateFormat  "${g}" "[INFO]: ${1}${nc}"
}

function warning() {
  dateFormat "${y}" "[WARNING]: ${1}${nc}"
}

function error() {
  dateFormat "${r}" "[ERROR]: ${1}${nc}"
}

function dateFormat() {
  echo -e "${1}[$(date '+%Y-%m-%d %H:%M:%S')] ${2}"
}

function emptyLine() {
  echo -e "\n"
}

#-------

function start() {
  load_env
  build

  nohup ./gradlew bootRun --args="\
  --telnet.connection.host=${TELNET_CONNECTION_HOST} \
  --telnet.connection.port=${TELNET_CONNECTION_PORT} \
  --telegram.bot.token=${TELEGRAM_BOT_TOKEN} \
  --telegram.bot.chat=${TELEGRAM_BOT_CHAT}" > ${LOG_FILE_PATH} 2>&1 &

  info "Application launched successfully!"
}

function build() {
    info "Building application from root directory"
    ./gradlew assemble
}

function stop() {
  info "Stop program..."
  PID=$(pgrep -f "org.gradle.wrapper.GradleWrapperMain bootRun --args")

  if [ -n "$PID" ]; then
    warning "Stop process by PID=$PID..."
    kill "$PID"
    info "Successfully stopped program."
  else
    e "❗ Process not found"
  fi
}

function check_upgrade() {
  info "Upgrade application..."
}

function upgrade() {
  info "Upgrade application..."
}

function load_env() {
    if [ -f "${ENV_PATH}" ]; then
      info "Starting the process of initializing environment variables"
      set -a
      source "$ENV_PATH"
      set +a
    else
      error "Env file '${ENV_PATH}' not found."
      exit 1
    fi
}

# Automatically detects JAVA_HOME if it is not set
if [ -z "$JAVA_HOME" ]; then
  JAVA_PATH=$(readlink -f "$(which java)")

  if [ -z "$JAVA_PATH" ]; then
    error "Java not installed. Install and try again later."
    exit 1
  fi

  JAVA_HOME=$(dirname "$(dirname "$JAVA_PATH")")
  export JAVA_HOME
  export PATH="$JAVA_HOME/bin:$PATH"

  info "JAVA_HOME installed in: $JAVA_HOME"
fi

# Check
java -version || {
  error "Error: Java is not available. Check environment variable."
  exit 1
}

case $ARG in

  '--help' | '-h')
    info "$HELP"
  ;;

  '--start' | '-s')
    start
  ;;

  '--stop')
    stop
  ;;

  '--check-upgrade')
    check_upgrade
  ;;

  '--upgrade' | '-u')
    upgrade
  ;;

  *)
    error "unknown argument: $ARG"
    emptyLine
    warning "$HELP"
esac
