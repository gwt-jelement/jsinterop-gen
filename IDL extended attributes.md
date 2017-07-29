___IDL Extended Attributes Supported by jsinterop-gen___

These additional extended attributes are supported by the generator:
- `GenericReturn`: for a method that returns type X, generate instead a return type of
<T> <T extends X>`. Applies to interface methods only (including callbacks when defined 
as callback interface).
- `GenericSub(T)`: substitutes the following type with the type parameter. Applies
to methods (for their return type), method arguments, interface attributes and dictionary 
members.
- `GenericParameter(T)`: makes the type a type parameterized with T. Applies to interfaces, 
dictionaries, methods (for their return type) if the return type is ObjectType,
method arguments if the type is ObjectType, and dictionary members if the type
is ObjectType
- `GenericExtend(T)` same as GenericParameter, but for the extended type.
- `Deprecated`: adds the `@Deprecated` annotation to the method or attribute. Currently 
applies to interface methods and attributes only.
- `JsTypeName(newTypeName)`: overrides the default @JsType name with newTypeName. Cuurently 
  applies to interfaces only.
- `JsPropertyName(newPropName)`: overrides the default @JsProperty name with newPropName. 
Currently applies to interface static attributes only, since those are generated as fields 
(others are generated with getters and setters)
- `JavaName(X)`: provides an alternate name X for a method, when the regular name
would conflict with an inherited or parent method name (for example, same method
with same signature, but static in one class and not the other).