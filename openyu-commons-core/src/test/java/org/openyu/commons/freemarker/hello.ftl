中文測試

FreeMarker Template example: ${message}  
 
=======================
===  County List   ====
=======================
<#list countries as country>
    ${country_index + 1}. ${country}
</#list>