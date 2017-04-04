#!/bin/bash

phabid=`echo "{\"ids\": [\"${DIFF_ID}\"]}" | arc call-conduit --conduit-uri https://phabricator.dm.nfl.com/ differential.querydiffs | jq -r ".[\"response\"][\"${DIFF_ID}\"][\"revisionID\"]"`
if [ "$phabid" != "null" ]; then
	echo "D$phabid"
fi

