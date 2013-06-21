
%function saveplot(directory,filename,datapoints,datavalues_of_tr,datavalues_of_notr,datavalues_hf_tr,datavalues_hf_notr)
		
	
	figure('visible','on'); %set to off

	colors = getColors(4);


	% values |  of/hf |tr/notr
	datavalues=zeros(size(datavalues_of_tr,2),2,2);

	datavalues(:,1,1)=datavalues_of_tr';
	datavalues(:,1,2)=datavalues_of_notr';

	datavalues(:,2,1)=datavalues_hf_tr';
	datavalues(:,2,2)=datavalues_hf_notr';

	datavalues


	lineStylesCell={'--','-', ':','-.'};

	index=1;
	legendCell = {};
	for featuretype=1:2 

		for transfertype=1:2 
      
			values = datavalues(:,featuretype,transfertype);
	  
			if min(sum(isnan(values))) == 0


			plotje = semilogx(datapoints, values);
			set(plotje,'LineStyle',lineStylesCell{index});
			set(plotje,'Marker','o');
			set(plotje, 'MarkerFaceColor',colors(index,:));

			set(plotje,'Color',colors(index,:),'LineWidth',2);

			hold on;


			%% for legend later
			switch transfertype
			  case 1
				legendstring1 = 'Transfer /';
			  case 2
				legendstring1 = 'No Transfer /';
			end
			switch featuretype
			  case 1
				legendstring2 = ' Automatic m.f.';
			  case 2
				legendstring2 = ' Handcrafted mf.';
			end

			legendCell{index} = strcat(legendstring1, legendstring2);


			%update colors, legend, line style
			index = index + 1;

			end

		end
	end



	% fit plot
	%axis tight


	% set x and y axes
	axis([min(datapoints)-0.5,max(datapoints)+1,0,1])

	% set tickmarks
	set(gca,'XTick',datapoints)
	set(gca,'YTick',[0:0.1:1])

	% set x and y axis labels
	x=xlabel('Number of labeled days');
	set(x, 'FontSize', 12);
	set(x,'FontWeight','bold'); 

	y=ylabel('Avg. Accuracy');
	set(y, 'FontSize', 12);
	set(y,'FontWeight','bold'); 

	% set legend
	l = legend(legendCell,'Location','SouthEast');
	set(y, 'FontSize', 11);
	set(y,'FontWeight','bold'); 

	% title
	t = title(strcat('House ', housename));
	set(t, 'FontSize', 13);
	set(t,'FontWeight','bold'); 


	% remove upper and right axes
	box off
  
	%remove legend box
	legend boxoff 

  % save plot
	fh = gcf; % get figure handle
	saveas(gcf, strcat(directory,strcat('plot',housename,'.pdf'));
	clear fh;
	hold off;

%end
