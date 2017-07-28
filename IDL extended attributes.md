___IDL Extended Attributes___

These additional extended attributes are supported by the generator:
- `GenericReturn`: for a method that returns type X, generate instead a return type of
<T> <T extends X>`. Applies to interface methods only (including callbacks when defined 
as callback interface).
- `Deprecated`: adds the `@Deprecated` annotation to the method or attribute. Currently 
applies to interface methods and attributes only.
- `JsTypeName(newTypeName)`: overrides the default @JsType name with newTypeName. Cuurently 
  applies to interfaces only.
- `JsPropertyName(newPropName)`: overrides the default @JsProperty name with newPropName. 
Currently applies to interface static attributes only, since those are generated as fields 
(others are generated with getters and setters)
- `GenericType(T)`: adds a generic type T to the type of an interface, a method return value or
a method argument.
