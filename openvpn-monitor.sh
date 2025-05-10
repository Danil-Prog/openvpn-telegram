#!/bin/bash

r='\033[0;31m' # red
g='\033[0;32m' # green
y='\033[0;33m' # yellow
nc='\033[0m'   # No Color

help="Available parameters:

  --help(-h)        :get list commands.
  --start(-s)       :start program local
  --stop            :stop program
  --check-upgrade   :checks if there are any updates available
  --upgrade         :upgrade application
"

arg=$1
env_path='.env'

#-------Logger

function log() {
  dateFormat  ${nc} "[LOG]: ${1}${nc}"
}

function info() {
  dateFormat  ${g} "[INFO]: ${1}${nc}"
}

function warning() {
  dateFormat ${y} "[WARNING]: ${1}${nc}"
}

function error() {
  dateFormat ${r} "[ERROR]: ${1}${nc}"
}

function dateFormat() {
  echo -e "${1}[$(date '+%Y-%m-%d %H:%M:%S')] ${2}"
}

function emptyLine() {
  echo -e "\n"
}

#-------

function start() {
  build
}

function build() {
    info "Building application from root directory"
    ./gradlew assemble
}

function stop() {
  info "Stop program..."
}

function check_upgrade() {
  info "Upgrade application..."
}

function upgrade() {
  info "Upgrade application..."
}

function if_env_not_exist() {
    if [ ! -f "${env_path}" ]; then
        error "File '.env' not found by path: ${env_path}" >&2
        exit 1
    fi
}

if_env_not_exist

case $arg in

  '--help' | '-h')
    info "$help"
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
    error "unknown argument: $arg"
    emptyLine
    warning "$help"
esac
