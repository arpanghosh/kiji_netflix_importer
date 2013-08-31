package com.redeyetechguy.kiji;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.kiji.mapreduce.KijiMapReduceJob;
import org.kiji.mapreduce.bulkimport.KijiBulkImportJobBuilder;
import org.kiji.mapreduce.input.MapReduceJobInputs;
import org.kiji.mapreduce.output.MapReduceJobOutputs;
import org.kiji.schema.KijiURI;

import java.io.IOException;


public class Driver
{
    private static Logger _logger = Logger.getLogger(Driver.class);

    public static void main( String[] args )
    {

        if (args.length < 1){
            System.out.println("Usage: Driver " +
                    "<HDFS_input_path>");
            return;
        }

        //Configuring log4j with defaults to log on console
        BasicConfigurator.configure();


        String inputFilePath = args[0];

        Configuration hBaseConfiguration =
                HBaseConfiguration.addHbaseResources(new Configuration(true));


        KijiMapReduceJob job = null;
        try {
            job = KijiBulkImportJobBuilder.create()
                    .withConf(hBaseConfiguration)
                    .withBulkImporter(MovieBulkImporter.class)
                    .withInput(MapReduceJobInputs.newTextMapReduceJobInput(new Path(inputFilePath)))
                    .withOutput(MapReduceJobOutputs
                            .newDirectKijiTableMapReduceJobOutput(KijiURI
                                    .newBuilder("kiji://localhost:2181/default/movies").build()))
                    .build();
        } catch (IOException e) {
            _logger.error(e);
            System.exit(1);
        }


        boolean isSuccessful = false;
        try{
            isSuccessful = job.run();
        }catch (Exception unknownException){
            _logger.error("Unknown Exception while running MapReduce Job", unknownException);
            System.exit(1);
        }

        String result = isSuccessful ? "Successful" : "Failure";
        _logger.info(result);

    }
}
