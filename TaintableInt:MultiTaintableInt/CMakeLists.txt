cmake_minimum_required(VERSION 3.23)
project(EC500)

set(CMAKE_CXX_STANDARD 14)

include_directories(hw0)
include_directories(hw0/code_review)

add_executable(EC500
        hw0/code_review/TaintableInt.h
        hw0/code_review/TaintableInt_test.cpp
        hw0/Obfuscated.cpp
        hw0/TaintableInt.h)
