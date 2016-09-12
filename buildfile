# Generated by Buildr 1.4.0, change to your liking
# Standard maven2 repository
repositories.remote << 'http://repo1.maven.org/maven2'

# Version number for this release
VERSION_NUMBER = '0.0.2'

desc 'Synacor Jetty modules'
define 'jettylog' do
        project.group = 'com.synacor.jetty'
        project.version = VERSION_NUMBER
		compile.with 'javax.servlet:servlet-api:jar:2.5'
		compile.with 'org.eclipse.jetty:jetty-annotations:jar:7.6.12.v20130726'
		compile.with 'org.eclipse.jetty:jetty-server:jar:7.6.12.v20130726'
		compile.with 'org.eclipse.jetty:jetty-util:jar:7.6.12.v20130726'
		compile.with 'log4j:log4j:jar:1.2.17'
		package(:jar, :id => 'jettylog')
		#compile.options.lint = 'all'
end

