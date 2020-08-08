
cmake_minimum_required(VERSION 3.4.1)
# add source code
AUX_SOURCE_DIRECTORY(. LIB_DIRSRCS_SOH)

add_library(
        sound
        SHARED
        ${LIB_DIRSRCS_SOH})