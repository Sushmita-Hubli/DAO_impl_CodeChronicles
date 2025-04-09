package com.example.demo.services.impl;

import java.io.File;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.FileService;

@Service
public class FileServiceimpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		String name=file.getOriginalFilename();
		//abc.png
		
		
		//random name generate file
		String randomID=UUID.randomUUID().toString();
		String filename1=randomID.concat(name.substring(name.lastIndexOf(".")));
		
		//Fullpath
		String filePath=path+File.separator+filename1;
		
		//create folder if not created
		File f=new File(path);
		if(!f.exists())
		{
			f.mkdir();
		}
		
		// Log the file path for debugging
	    System.out.println("Saving file to: " + filePath);
	    
		
		//file copy
		Files.copy(file.getInputStream(), Paths.get(filePath));
		return filename1;
	
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		   try {
		        Path filePath = Paths.get(path).resolve(fileName);
		        System.out.println("Attempting to read file from: " + filePath.toAbsolutePath());
		        
		        File file = filePath.toFile();
		        if (!file.exists()) {
		            System.out.println("WARNING: File does not exist: " + filePath.toAbsolutePath());
		            throw new FileNotFoundException("File not found: " + fileName);
		        }
		        
		        return new FileInputStream(file);
		    } catch (Exception e) {
		        System.out.println("Error accessing file: " + e.getMessage());
		        throw new FileNotFoundException("Error accessing file: " + fileName + " - " + e.getMessage());
		    }
	}
}
