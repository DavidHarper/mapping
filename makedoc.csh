#!/bin/csh -f

if ( ! -d doc ) then
  mkdir doc
endif

javadoc -d doc -author -version -sourcepath . com.obliquity.mapping
