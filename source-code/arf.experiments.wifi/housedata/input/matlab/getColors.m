% get nicer colors than built-in ones

function myColors = getColors(number) 

	colors=[  	0.647059 0.164706 0.164706;
				0.392157 0.584314 0.929412;
				0 0 0.545098;
				0 0.392157 0;
				0.854902 0.647059 0.12549; % dark yellow
				1 0.411765 0.705882; %pink
				0.517647 0.439216 1;
				0.466667 0.533333 0.6;
			];
			
	while (number > size(colors,1))
		colors = [colors;colors];
	end
	
	myColors= colors(1:number,:);

end