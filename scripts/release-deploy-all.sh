#!/bin/bash

./gradlew build bintrayUpload -PbintrayUser=$BINTRAY_USERNAME -PbintrayKey=$BINTRAY_API_KEY -PdryRun=false