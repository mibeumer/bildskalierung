#!/bin/bash

# Delete the unpacked war directory if it exists:
WEBAPP_DIR=/usr/share/shop_bildskalierung/webapps/de.buch.shop.bildskalierung.application

if [ -d "$WEBAPP_DIR" ]; then
  rm -rf $WEBAPP_DIR
fi
