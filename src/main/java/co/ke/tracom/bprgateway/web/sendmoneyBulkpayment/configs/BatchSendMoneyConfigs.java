//package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.configs;
//
//import co.ke.tracom.bprgateway.core.util.RRNGenerator;
//import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
//import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
//import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
//import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service.BulkSendMoneyService;
//import lombok.AllArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
//import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//
//@Configuration
//@EnableBatchProcessing
//@AllArgsConstructor
//public class BatchSendMoneyConfigs {
//    private JobBuilderFactory jobBuilderFactory;
//    private StepBuilderFactory stepBuilderFactory;
//    private final MoneySendRepository moneySendRepository;
//   private BulkSendMoneyService bulkSendMoneyService;
//    private SendMoneyRequest sendMoneyRequest;
//
//
//    public FlatFileItemReader<SendMoneyRequest> processBulkMoneyreader(){
//        FlatFileItemReader<SendMoneyRequest>fileItemReader=new FlatFileItemReader<>();
//        fileItemReader.setResource(new FileSystemResource("src/main/resource/TESTS.CSV"));
//        fileItemReader.setName("CSV_READER");
//        fileItemReader.setLinesToSkip(1);
//        fileItemReader.setLineMapper(lineMapper());
//        return fileItemReader;
//    }
//
//    public
//
//    String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
//    private Resource outputResource = new FileSystemResource("output/outputData.csv");
//    private LineMapper<SendMoneyRequest> lineMapper() {
//        DefaultLineMapper lineMapper=new DefaultLineMapper<>();
//        DelimitedLineTokenizer delimiter=new DelimitedLineTokenizer();
//        delimiter.setDelimiter(",");
//        delimiter.setStrict(false);
//        delimiter.setNames("id","firstName","lastName","email","gender","contactNo","country","doB");
//        BeanWrapperFieldSetMapper<SendMoneyRequest> beanWrapper=new BeanWrapperFieldSetMapper<>();
//        beanWrapper.setTargetType(SendMoneyRequest.class);
//        lineMapper.setLineTokenizer(delimiter);
//        lineMapper.setFieldSetMapper(beanWrapper);
//        return lineMapper;
//    }
//    @Bean
//    public SendMoneyResponse customerProcessing(){
//        return  bulkSendMoneyService.bulkSendMoneyProcess(sendMoneyRequest,transactionRRN);
//    }
////    @Bean
////    public <> repositoryItemWriter(){
////        RepositoryItemWriter<MoneySend> repository=new RepositoryItemWriter<>();
////        repository.setRepository(moneySendRepository);
////        repository.setMethodName("save");
////        return repository;
////    }
//
//    @Bean
//    public FlatFileItemWriter<SendMoneyResponse>flatFileItemWriter(){
//        FlatFileItemWriter<SendMoneyResponse>flatFilewriter=new FlatFileItemWriter<>();
//        flatFilewriter.setResource(outputResource);
//        flatFilewriter.setAppendAllowed(true);
//        flatFilewriter.setLineAggregator(new DelimitedLineAggregator<>() {{
//            setDelimiter("");
//            setFieldExtractor(new BeanWrapperFieldExtractor<SendMoneyResponse>(){
//
//            });
//        }
//        });
//        return flatFilewriter;
//    }
//    @Bean
//    public Step processBulksendMoneyStep1(){
//        return stepBuilderFactory.get("CSV_NAME").<SendMoneyRequest, SendMoneyResponse>chunk(10)
//                .reader(processBulkMoneyreader())
//                .processor((ItemProcessor<? super SendMoneyRequest, ? extends SendMoneyResponse>) customerProcessing())
//                .writer(flatFileItemWriter())
//                .taskExecutor(executor())
//                .build();
//    }
//
//
//    public Step processSendSmsstep2(){
//        return stepBuilderFactory.get("SMS_SEND").chunk(10)
//                .reader()
//    }
//
//
//
//    @Bean
//    public Job runJobs(){
//        return jobBuilderFactory.get("importCustomers")
//                .flow(processBulksendMoneyStep1())
//                .end().build();
//    }
//    @Bean
//    public TaskExecutor executor(){
//        SimpleAsyncTaskExecutor taskExecutor=new SimpleAsyncTaskExecutor();
//        taskExecutor.setConcurrencyLimit(10);
//        return taskExecutor;
//    }
//}
