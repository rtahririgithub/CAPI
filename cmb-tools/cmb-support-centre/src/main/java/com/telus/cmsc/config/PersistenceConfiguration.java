package com.telus.cmsc.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.telus.cmsc.dao.ArtifactDao;
import com.telus.cmsc.dao.ArtifactDaoJdbcImpl;
import com.telus.cmsc.dao.ArtifactGroupDao;
import com.telus.cmsc.dao.ArtifactGroupDaoJdbcImpl;
import com.telus.cmsc.dao.ArtifactNotificationDao;
import com.telus.cmsc.dao.ArtifactNotificationDaoImpl;
import com.telus.cmsc.dao.EnvironmentDao;
import com.telus.cmsc.dao.EnvironmentDaoJdbcImpl;
import com.telus.cmsc.dao.ReferenceVersionDao;
import com.telus.cmsc.dao.ReferenceVersionDaoJdbcImpl;

/**
 * @author Pavel Simonovsky
 *
 */

@Configuration
@EnableTransactionManagement
public class PersistenceConfiguration {

	@Autowired
	private ApplicationContext context;
	
	@Resource(mappedName="jdbc/cmb/cmbsc")
	private DataSource cmbscDataSource;
	
	@Resource(mappedName="jdbc/cmb/dinfr")
	private DataSource armxDevelopmentDataSource;

	@Resource(mappedName="jdbc/cmb/sinfr")
	private DataSource armxStagingDataSource;

	@Resource(mappedName="jdbc/cmb/rinfr")
	private DataSource armxProductionDataSource;

	/*
	@Bean
	public DataSource cmbscDataSource() {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@D070296:1521:CMBSC");
		dataSource.setUsername("CMBSC_ADMIN");
		dataSource.setPassword("CMBSC_ADMIN");

		return dataSource;
	}

	@Bean
	public DataSource armxDevelopmentDataSource() {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@rac_ln98250-scan.corp.ads:41521/DINFR.WORLD");
		dataSource.setUsername("ARMX_APP");
		dataSource.setPassword("ARMX_APP");

		return dataSource;
	}
	
	@Bean
	public DataSource armxStagingDataSource() {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@rac_ln98300-scan.corp.ads:41521/SINFR.WORLD");
		dataSource.setUsername("t854694");
		dataSource.setPassword("nj8822k");

		return dataSource;
	}

	@Bean
	public DataSource armxProductionDataSource() {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@rac_lp97200-scan.corp.ads:41521/RINFR.WORLD");
		dataSource.setUsername("t854694");
		dataSource.setPassword("to2k2td");

		return dataSource;
	}
	
	*/
	
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(cmbscDataSource); 
	}

	@Bean
	public EnvironmentDao environmentDao() {
		return new EnvironmentDaoJdbcImpl(cmbscDataSource);
	}

	@Bean
	public ArtifactDao artifactDao() {
		return new ArtifactDaoJdbcImpl(cmbscDataSource);
	}
	
	@Bean
	public ArtifactGroupDao artifactGroupDao() {
		return new ArtifactGroupDaoJdbcImpl(cmbscDataSource);
	}
	
	@Bean 
	public ReferenceVersionDao referenceVersionDao() {
		return new ReferenceVersionDaoJdbcImpl(cmbscDataSource);
	}
	
	@Bean(name="developmentNotificationDao")
	public ArtifactNotificationDao developmentNotificationDao() {
		return new ArtifactNotificationDaoImpl(armxDevelopmentDataSource);
	}
	
	@Bean(name="stagingNotificationDao")
	public ArtifactNotificationDao stagingNotificationDao() {
		return new ArtifactNotificationDaoImpl(armxStagingDataSource);
	}
	
	@Bean(name="productionNotificationDao")
	public ArtifactNotificationDao productionNotificationDao() {
		return new ArtifactNotificationDaoImpl(armxProductionDataSource);
	}
	
	
}
