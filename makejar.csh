#!/bin/csh -f

jar cvf mapping.jar OSGBApplet.class OSGBForm.class \
                    OSNI2OSGB.class OSNI2OSGBForm.class \
                    OSNIApplet.class OSNIForm.class \
                    Separator.class \
                    ThreeDPanel.class \
                    com/obliquity/mapping/*.class
