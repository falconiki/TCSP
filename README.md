__**TCSP tree converter**__
======

This is simulator that combines two json trees, merges them in appropriate result 
and retrieve the result in a json format.
  
These are the steps for testing this simulator: 
1. Run the TcspApplication main class (make sure 8080 port is available)
2. Go to this url: http://localhost:8080/swagger-ui.html and expand TCSP resource dropdown
3. First, expand the POST /swisscom/tcsp and choose "Try it out" without changing anything to test
 if the initial request is working and check the **Response Body**
There is a request parameter "**folder**", which has a default value of "same-attribute-diff-children". 
For testing purposes each folder contains different structure of the files initial_tree.json and new_tree.json.   
You can choose different option from folders under main/resources/ instead of the default one 'same-attribute-diff-children'
4. Confirm that the response has correctly combined the two trees initial_tree and new_tree from the choosen folder
5. Possible options for the parameter 'folder' to test other combinations:
- diff-attribute-diff-children
- diff-attribute-same-children
- same-attribute-diff-children
- same-trees-updated-children
- same-trees
- large-trees

Additionally, these options are covered with integration tests in **TcspApplicationTests**
