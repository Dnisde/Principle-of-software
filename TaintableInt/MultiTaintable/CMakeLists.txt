cmake_minimum_required(VERSION 3.23)
project(hw0_12)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(hw0_12
        Taintable.cpp
        Taintable.h)
