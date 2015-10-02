namespace java org.openyu.commons.thrift
namespace cpp Openyu.Commons.Thrift

service CommonsCoreService {
  bool booleanHelper_toBoolean(1:string value, 2:bool defaultValue)
  
  string booleanHelper_toString(1:bool value)
  
  i32 booleanHelper_toInt(1:bool value)
}