# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.15

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake

# The command to remove a file.
RM = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/denisserbin/compiler_lab_6

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/denisserbin/compiler_lab_6/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/compiler_lab_6.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/compiler_lab_6.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/compiler_lab_6.dir/flags.make

CMakeFiles/compiler_lab_6.dir/main.c.o: CMakeFiles/compiler_lab_6.dir/flags.make
CMakeFiles/compiler_lab_6.dir/main.c.o: ../main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/denisserbin/compiler_lab_6/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/compiler_lab_6.dir/main.c.o"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/compiler_lab_6.dir/main.c.o   -c /Users/denisserbin/compiler_lab_6/main.c

CMakeFiles/compiler_lab_6.dir/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/compiler_lab_6.dir/main.c.i"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/denisserbin/compiler_lab_6/main.c > CMakeFiles/compiler_lab_6.dir/main.c.i

CMakeFiles/compiler_lab_6.dir/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/compiler_lab_6.dir/main.c.s"
	/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/denisserbin/compiler_lab_6/main.c -o CMakeFiles/compiler_lab_6.dir/main.c.s

# Object files for target compiler_lab_6
compiler_lab_6_OBJECTS = \
"CMakeFiles/compiler_lab_6.dir/main.c.o"

# External object files for target compiler_lab_6
compiler_lab_6_EXTERNAL_OBJECTS =

compiler_lab_6: CMakeFiles/compiler_lab_6.dir/main.c.o
compiler_lab_6: CMakeFiles/compiler_lab_6.dir/build.make
compiler_lab_6: CMakeFiles/compiler_lab_6.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/Users/denisserbin/compiler_lab_6/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable compiler_lab_6"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/compiler_lab_6.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/compiler_lab_6.dir/build: compiler_lab_6

.PHONY : CMakeFiles/compiler_lab_6.dir/build

CMakeFiles/compiler_lab_6.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/compiler_lab_6.dir/cmake_clean.cmake
.PHONY : CMakeFiles/compiler_lab_6.dir/clean

CMakeFiles/compiler_lab_6.dir/depend:
	cd /Users/denisserbin/compiler_lab_6/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/denisserbin/compiler_lab_6 /Users/denisserbin/compiler_lab_6 /Users/denisserbin/compiler_lab_6/cmake-build-debug /Users/denisserbin/compiler_lab_6/cmake-build-debug /Users/denisserbin/compiler_lab_6/cmake-build-debug/CMakeFiles/compiler_lab_6.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/compiler_lab_6.dir/depend

