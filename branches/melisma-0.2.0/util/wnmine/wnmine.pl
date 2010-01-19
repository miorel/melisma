#!/usr/bin/perl -l

#
# This script takes a WordNet (3.0) database as standard input and extracts
# words from it to standard output.
#
# The current version omits words not composed exclusively of lowercase
# letters.
#

use warnings;
use strict;

%_ = ();

while(<>) {
	next if /^\s/;
	@_ = split(/\s+/, $_);
	/[^a-z]/ or $_{$_} = 1 for map {$_[2 * ($_ + 1)]} 1..hex($_[3]);
}

print for sort keys %_;
