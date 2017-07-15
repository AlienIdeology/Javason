# Javason
A light-weight Json parser and serializer for Java.

## Description
This Json parser parses json strings into JsonArray and JsonObject.
It is straight forward and easy to use. This also contains immutable Json array and object.
There is also a PureJsonArray, which is easier for iterating through a json array with the
same generic type.
This library supports serialize and deserialize json objects and arrays into Java objects, arrays and collections.
The serialization process can be customized by setting SerializeHandler and SerializeNamingPolicy. It also
supports annotations to help the process.

## TODO
1. Better error handling for JsonParser (Missing `,`)
2. Serialize and Deserialize arrays and collections.
3. Accept parametric constructors for deserialize objects.
4. JsonTokenizer for tokenize keys and values.