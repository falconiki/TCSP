__**TCSP tree converter**__
======

This is a simulator that combines two json trees, merges them in an appropriate result 
and retrieves the result in a json format.
  
These are the steps for testing this simulator: 
1. Run the TcspApplication main class (make sure 8080 port is available)
2. Go to this url: http://localhost:8080/swagger-ui.html and expand TCSP resource dropdown
3. First, expand the POST /swisscom/tcsp and choose "Try it out" without changing anything in order to test
 if the initial request is working. Check and validate the **Response Body**
The endpoint accepts a parameter "**folder**", which has a default value of "same-attribute-diff-children". 
For testing purposes each folder contains different structure of the files initial_tree.json and new_tree.json.   
You can choose different option from the folders under main/resources/ instead of the default one 'same-attribute-diff-children'
4. Confirm that the response has correctly combined initial_tree.json and new_tree.json from the chosen folder
5. Possible options for the parameter 'folder' to test other combinations:
- diff-attribute-diff-children
- diff-attribute-same-children
- same-attribute-diff-children
- same-trees-updated-children
- same-trees
- large-trees
- no-common-children
- no-attributes
- same-id-diff-type

Additionally, these options are covered with integration tests in **TcspApplicationTests**
