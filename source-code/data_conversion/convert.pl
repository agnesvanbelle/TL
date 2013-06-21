#!/usr/bin/perl

use strict;
use warnings;

use Time::Local;

my %month = (
	'Jan' => 0,
	'Feb' => 1,
	'Mar' => 2,
	'Apr' => 3,
	'May' => 4,
	'Jun' => 5,
	'Jul' => 6,
	'Aug' => 7,
	'Sep' => 8,
	'Oct' => 9,
	'Nov' => 10,
	'Dec' => 11
);

my %mapping_type_1 =
(
	7 => 4
);

my %activities_type_2 =
(
	'Work at home' => 1,
	'Going out to work' => 5,
	'Eating' => 10,
	'Toileting' => 15,
	'Bathing' => 20,
	'Grooming' => 25,
	'Dressing' => 30,
	'Washing hands' => 35,
	'Taking medication' => 40,
	'Sleeping' => 45,
	'Talking on telephone' => 50,
	'Resting' => 55,
	'Preparing breakfast' => 60,
	'Preparing lunch' => 65,
	'Preparing dinner' => 70,
	'Preparing a snack' => 75,
	'Preparing a beverage' => 80,
	'Washing dishes' => 85,
	'Putting away dishes' => 90,
	'Putting away groceries' => 95,
	'Cleaning' => 100,
	'Doing laundry' => 105,
	'Putting away laundry' => 110,
	'Taking out the trash' => 115,
	'Lawnwork' => 120,
	'Pet care' => 122,
	'Home education' => 125,
	'Going out to school' => 130,
	'Watching TV' => 135,
	'Listening to music' => 140,
	'Going out for entertainment' => 145,
	'Working at computer' => 147,
	'Going out to exercise' => 150,
	'Going out for shopping' => 155,
	'Other' => 160
);

sub convert_type_1 # Tim van Kasteren's dataset
{
	open(IN,  "<$_[0]");
	open(OUT, ">$_[1]");

	while (<IN>)
	{
		my @tokens = split(/\t/, $_);
		$tokens[2] =~ s/\n//;

		$tokens[0] =~ /^(.+?)-(.+?)-(.+?) (.+?):(.+?):(.+?)$/; # $mday, $mon, $year, $hour, $min, $sec
		my $start = timegm($6, $5, $4, $1, $month{$2}, $3);    # $sec, $min, $hour, $mday, $mon, $year

		$tokens[1] =~ /^(.+?)-(.+?)-(.+?) (.+?):(.+?):(.+?)$/; # $mday, $mon, $year, $hour, $min, $sec
		my $end = timegm($6, $5, $4, $1,  $month{$2}, $3);     # $sec, $min, $hour, $mday, $mon, $year
	
		print OUT $start . "\t" . ($end - $start) . "\t";
		
		if ($mapping_type_1{$tokens[2]})
		{
			print OUT $mapping_type_1{$tokens[2]} . "\n";
		}
		else
		{
			print OUT $tokens[2] . "\n";
		}
	}

	close(IN);
	close(OUT);

	system("sort -s -n -k 1 $_[1] -o $_[1]");
}

sub convert_type_2 # MIT's dataset
{
	open(IN,  "<$_[0]");
	open(OUT_SENSORS, ">$_[0].sensors");
	open(OUT_LABELS,  ">$_[0].activities");

	while (<IN>)
	{
		# ".labels"

		my @tokens = split(/,/, $_);
		$tokens[3] =~ s/\n//;

		$tokens[1] =~ /^(.+?)\/(.+?)\/(.+?)$/; # $mon, $mday, $year
		my ($mon, $mday, $year) = ($1, $2, $3);

		$tokens[2] =~ /^(.+?):(.+?):(.+?)$/; # $hour, $min, $sec
		my ($start_hour, $start_min, $start_sec) = ($1, $2, $3);

		$tokens[3] =~ /^(.+?):(.+?):(.+?)$/; # $hour, $min, $sec
		my ($end_hour, $end_min, $end_sec) = ($1, $2, $3);

		if ($mon == 4 and $mday == 31)
		{
			$mon  = 5;
			$mday = 1;
		}

		my $start = timegm($start_sec, $start_min, $start_hour, $mday, $mon - 1, $year);
		my $end   = timegm($end_sec,   $end_min,   $end_hour,   $mday, $mon - 1, $year);
		
		print OUT_LABELS $start . "\t" . ($end - $start) . "\t" . $activities_type_2{$tokens[0]} . "\n";

		# ".sensors"

		my @sensors;

		@tokens = split(/,/, <IN>); # Sensor IDs

		my $i = 0;

		foreach (@tokens)
		{
			my $id = $_;
			$id =~ s/\n//;

			$sensors[$i++] = [undef, undef, $id];
		}

		@tokens = split(/,/, <IN>); # Sensor names, do nothing
		@tokens = split(/,/, <IN>); # Starting times

		$i = 0;

		foreach (@tokens)
		{
			$_ =~ s/\n//;
			$_ =~ /^(.+?):(.+?):(.+?)$/; # $hour, $min, $sec
			($start_hour, $start_min, $start_sec) = ($1, $2, $3);

			$start = timegm($start_sec, $start_min, $start_hour, $mday, $mon - 1, $year);
			$sensors[$i++][0] = $start;
		}

		@tokens = split(/,/, <IN>); # Ending times

		$i = 0;

		foreach (@tokens)
		{
			$_ =~ s/\n//;
			$_ =~ /^(.+?):(.+?):(.+?)$/; # $hour, $min, $sec
			($end_hour, $end_min, $end_sec) = ($1, $2, $3);

			$end = timegm($end_sec, $end_min, $end_hour, $mday, $mon - 1, $year);
			$sensors[$i][1] = $end - $sensors[$i][0];

			$i++;
		}

		# Finally writing in ".sensors":

		foreach (@sensors)
		{
			print OUT_SENSORS $_->[0] . "\t" . $_->[1] . "\t" . $_->[2] . "\n";
		}
	}

	close(IN);
	close(OUT_SENSORS);
	close(OUT_LABELS);

	system("sort -s -n -k 1 $_[0].sensors -o $_[0].sensors");
	system("sort -s -n -k 1 $_[0].activities -o $_[0].activities");
}

convert_type_1('houseA-as.txt', 'houseA.activities');
convert_type_1('houseA-ss.txt', 'houseA.sensors');
convert_type_1('houseB-as.txt', 'houseB.activities');
convert_type_1('houseB-ss.txt', 'houseB.sensors');
convert_type_1('houseC-as.txt', 'houseC.activities');
convert_type_1('houseC-ss.txt', 'houseC.sensors');

convert_type_2('subject1/activities_data.csv');
convert_type_2('subject2/activities_data.csv');
