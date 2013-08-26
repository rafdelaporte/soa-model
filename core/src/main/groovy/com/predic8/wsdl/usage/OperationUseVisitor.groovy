/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.wsdl.usage;

import com.predic8.schema.*
import com.predic8.schema.creator.AbstractSchemaCreator;
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.facet.*
import com.predic8.wsdl.AbstractPortTypeMessage
import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation

class OperationUseVisitor extends AbstractSchemaCreator<OperationUseVisitorContext> {
	
  public OperationUseVisitorContext visitSchema4Operation(Operation op, OperationUseVisitorContext ctx) {
		op.input.message.parts.each {part ->
			ctx.opUsage = new OperationUsage(operation: op, input: true)
			part.element? (part.element.create(this, ctx)) : (part.type.create(this, ctx))
		}
		
		op.output.message.parts.each {part ->
			ctx.opUsage = new OperationUsage(operation: op, output: true)
			part.element? part.element.create(this, ctx) : part.type.create(this, ctx)
		}
		ctx
	}
	
  public void createElement(Element element, OperationUseVisitorContext ctx) {
		ctx.elementsInfo[element]? (ctx.elementsInfo[element] << ctx.opUsage) : (ctx.elementsInfo[element] = [ctx.opUsage]) 
		super.createElement(element, ctx)
  }

  public void createComplexType(ComplexType complexType, OperationUseVisitorContext ctx) {
		ctx.complexTypesInfo[complexType]? (ctx.complexTypesInfo[complexType] << ctx.opUsage) : (ctx.complexTypesInfo[complexType] = [ctx.opUsage])
		super.createComplexType(complexType, ctx)
  }

	public void createSimpleType(SimpleType simpleType, OperationUseVisitorContext ctx) {
		ctx.simpleTypesInfo[simpleType]? (ctx.simpleTypesInfo[simpleType] << ctx.opUsage) : (ctx.simpleTypesInfo[simpleType] = [ctx.opUsage])
		/**Most of the time a simpleType refers to a buil-in schema type.
		 * In other cases the following types will be ignored. 
		 */
	}
	
}