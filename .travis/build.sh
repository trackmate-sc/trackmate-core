#!/bin/sh
curl -fsLO https://raw.githubusercontent.com/scijava/scijava-scripts/master/travis-build.sh
sh travis-build.sh $encrypted_c11fc49e9f83_key $encrypted_c11fc49e9f83_iv
