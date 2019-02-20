<h3>Intro:</h3>
This automation demo will do the following tests:
<ul>
	<li>User Interface Test (GUI)</li>
	<li>Functionality Test</li>
	<li>Negative and Postiive Tests</li>
	<li>API Test</li>

</ul>
All test cases will have snopshot at every steps and test result will be genereted in the file.



<h3> Environment Setup:</h3>

In order to install the NSS-TODO-List (extended) application:
<ul>
<li>Make sure that you have a server installed that can serve PHP. Apache would be a good option.</li>
<li>Make sure you have a PHP 5.2.5 or higher installed on your system.</li>
<li><strong>Note:</strong> You can download an installer like XAMPP for the above two requirements.</li>
<li>Start the Apache server if it is not already running. Verify by going to localhost in your browser</li>
<li>Checkout the project and put all its contents in the 'htdocs' (/www) folder of Apache.You may need to mount same via XAMPP</li>
<li><strong>Note:</strong> You may want to explore your file read/write permissions on your local file system.</li>
<li>Go to http://localhost/nss-todo-automation/index.php (or whatever name you have given to the folder containing all of the project files) and access the application.</li>
<li>Get Selenium Chrome WebDriver</li>
<li>Download and install Eclipse</li>
</ul>

<h3> Execution instructions:</h3>
<ul>
<li>Open project with eclipse from the same directory as downloaded project path</li>
<li> Give <strong>webDriverPath</strong> path to your WebDiver</li>
<h3> Also change the following variable values to path of the project:</h3>
<li><strong>webDriverPath</strong></li>
<li><strong>baseUrl</strong></li> 
<li><strong>reportDest</strong></li> 
<li><strong>xpath</strong></li>
<strong>fullReportDirectory</strong></ul>

<h5>Test reports and screenshots will be saved at <strong>localhost/nss-todo-automation/test-results/</strong></h5>
<h5>Test reports and screenshots will be saved at <strong>localhost/nss-todo-automation/testcases/test-case-example.xls</strong></h5>
<h5>There is a video of execution at <strong>localhost/nss-todo-automation/test-results/run-demo.mov</strong></h5>


<h4>Build on enviroment</h4>
<ul>
<li>Eclipse: Version: 2018-12 (4.10.0), Build id: 20181214-0600</li>
<li>Java version "1.8.0_91"</li>
<li>PHP Version 7.3.0"</li>
</ul>



