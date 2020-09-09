#!/bin/bash

BASEDIR="$(git rev-parse --show-toplevel)"
TARGET="$BASEDIR/.git/hooks/pre-commit"


cat << EOT > "$TARGET"
for file in \$(find -type f -name "*.java"); do
	clang-format --style=file \$file -i
	git add \$file
done

EOT

chmod +x "$TARGET"


