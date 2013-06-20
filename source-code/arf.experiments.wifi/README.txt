About the directory structure in arf.experiments.wifi/housedata

input:  all input files
        this directory should be edited with care
output: output files - will be erased after running runTransferAlgorithm()

  input/HF:     "Handcrafted Metafeatures" input data
  input/OF:     "Our Metafeatures" input data
  
                input/HF and input/OF should use following folder
                names for houses:
                houseInfo<house-1-id>, houseInfo<house-2-id>, etc.
                
  input/matlab: matlab scripts needed if you later want to use 
                the generated matlab scripts in /output/matlab/ directly


  input/HF/houseInfoA: 
  
      ==> IMPORTANT!
      
      should contain:
        actionMapA.txt
        houseA-as.txt
        houseA-ss.txt
        sensormapA-ids.txt      
                      
      same for input/OF/houseInfoA (content of files can differ, but the format not)
      and for all houses this should be (replace letter A with B, C etc.)
      
      You can use anything instead of A, B, C.. however, the
      house-id used in the folder name houseInfo<house-id> should  mathc
      with the house-id used for these filenames
      

  
  output/houseInfoA:
        contains results for house A
        
        similar for house B, C, etc.
  
    output/houseInfoA/A6:
          contains results for house A where the train set
          size is 5 (+1 for the test set: one example is used for testing)
          
          similar for A11, A21 etc.
   
      output/houseInfoA/A6/OF:
            contains results for case of "Our Metafeatures" for train/test set size of 6
            generated from input/OF
            
            similar for output/houseInfoA/A6/HF  but contains results for "Handcrafted Metafeatures" 
      
        output/houseInfoA/A5/OF/NOTRANSFER:
              contains results for case of "Our Metafeatures" for train/test set size of 6 
              and a case of no transfer learning
              
              similar for output/houseInfoA/A5/OF/TRANSFER:
              but there transfer learning has been used
              
          output/houseInfoA/A5/HF/NOTRANSFER/TEST:
              contains svm-ready test sets for "Our Metafeatures" for train/test set size of 
              6 and case of no transfer learning
              The name of the files represent the training instances 
            
  output/matlab :
    contains generated scripts that plot results:
    transfer X notransfer X our metafeatures X handcrafted metafeatures
