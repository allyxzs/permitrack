#!/bin/sh
# Gradle Wrapper bootstrap for Gradle 8.9.
set -eu
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
exec gradle -p "$APP_HOME" "$@"
