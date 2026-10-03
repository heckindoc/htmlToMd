# htmlToMd
### Author: Heckindoc

This library uses both the commonmark-java and jsoup libraries as dependencies.

Going from .md to HTML is handled using any number of libraries, one of the most popular being the commonmark-java library which is still actively maintained. However, I was needing to go the other direction and take HTML documents and convert them into .md. There are fewer options available for this. Other options that exist include:
- flexmark-java --> as of when htmlToMd was made, flexmark was not actively being maintained. It has a lot of functionality, but it can be a bit cumbersome .
- copy-down --> a direct port from JS to java, honestly a really great and light weight option.

The goal of this library it to be light weight and easily usable, accepting a string input and providing a string output with the converted text. 
