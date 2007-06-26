#!/bin/csh -f

jar cvfm mapping.jar Manifest.mf \
		    OSGBApplet.class OSGBForm.class \
                    OSGBReverseApplet.class OSGBReverseForm.class \
                    OSNI2OSGB.class OSNI2OSGBForm.class \
                    OSNIApplet.class OSNIForm.class \
                    Separator.class \
                    ThreeDPanel.class \
		    test/*.class
                    com/obliquity/mapping/*.class
