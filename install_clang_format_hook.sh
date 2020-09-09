#!/bin/bash

BASEDIR="$(git rev-parse --show-toplevel)"
TARGET="$BASEDIR/.git/hooks/pre-commit"

cat << EOT > "$TARGET"
TO_BE_FORMATTED="\$(git diff --name-only --cached | egrep ".+\.java")"

if [ \${#TO_BE_FORMATTED} -eq 0 ]; then
	echo "No java files detected. Skipping clang-format."
	exit 0
fi


echo "Fixing up \$TO_BE_FORMATTED"

for file in "\${TO_BE_FORMATTED}"; do
	echo "Clang formatting \$file"
	clang-format --style=file \$file -i
	git add \$file
done

EOT

chmod +x "$TARGET"

