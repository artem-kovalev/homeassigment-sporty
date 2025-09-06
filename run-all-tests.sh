#!/usr/bin/env bash
set -e  # exit immediately if a command fails

# Find all directories containing "mvnw"
projects=$(find . -mindepth 2 -maxdepth 2 -type f -name "mvnw" -exec dirname {} \;)

for project in $projects; do
  echo ">>> Running tests in $project"
  (cd "$project" && ./mvnw test)
done

echo ">>> All tests finished successfully!"
