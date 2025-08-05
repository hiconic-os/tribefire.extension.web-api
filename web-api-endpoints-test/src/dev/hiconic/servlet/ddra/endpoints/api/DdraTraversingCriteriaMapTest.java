// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package dev.hiconic.servlet.ddra.endpoints.api;

import static com.braintribe.model.generic.typecondition.TypeConditions.isKind;
import static com.braintribe.model.generic.typecondition.TypeConditions.orTc;
import static com.braintribe.testing.junit.assertions.gm.assertj.core.api.GmAssertions.assertThat;

import org.junit.Test;

import com.braintribe.model.DdraEndpointDepth;
import com.braintribe.model.DdraEndpointDepthKind;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.pr.criteria.matching.StandardMatcher;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.reflection.BaseType;
import com.braintribe.model.generic.reflection.StandardCloningContext;
import com.braintribe.model.generic.reflection.StrategyOnCriterionMatch;
import com.braintribe.model.generic.tools.AbstractStringifier;
import com.braintribe.model.generic.typecondition.basic.TypeKind;

public class DdraTraversingCriteriaMapTest {

	private static final int DEPTH = 1;

	@Test
	public void testCloningWithDepth() {
		TraversingCriterion criterion = criterion();
		GmTestTag tag = tag();

		GmTestTag clonedModel = (GmTestTag) cloneWithTraversingCriterion(criterion, tag);

		String printed = new TagPrinter(clonedModel).print();
		assertThat(printed) //
				.contains("Tag 1") //
				.contains("Tag 2") //
				.doesNotContain("Tag 3");
	}

	private TraversingCriterion criterion() {
		DdraEndpointDepth depth = DdraEndpointDepth.T.create();
		depth.setCustomDepth(DEPTH);
		depth.setKind(DdraEndpointDepthKind.custom);

		DdraTraversingCriteriaMap ddraMap = new DdraTraversingCriteriaMap();
		ddraMap.addDefaultCriterion(DdraEndpointDepthKind.shallow, tcShallow());
		ddraMap.getCriterion(depth);

		return ddraMap.getCriterion(depth);
	}

	private TraversingCriterion tcShallow() {
		// @formatter:off
		return TC.create()
				.conjunction()
					.property()
					.typeCondition(
						orTc(
							isKind(TypeKind.collectionType),
							isKind(TypeKind.entityType)
						)
					)
				.close()
				.done();
		// @formatter:on
	}

	private GmTestTag tag() {
		GmTestTag t1 = createTag(1);
		GmTestTag t2 = createTag(2);
		GmTestTag t3 = createTag(3);
		GmTestTag t4 = createTag(4);

		t1.setTag(t2);
		t2.setTag(t3);
		t3.setTag(t4);

		return t1;
	}

	private GmTestTag createTag(int i) {
		GmTestTag result = GmTestTag.T.create();
		result.setName("Tag " + i);
		return result;
	}

	private static Object cloneWithTraversingCriterion(TraversingCriterion traversingCriterion, Object result) {
		StandardMatcher matcher = new StandardMatcher();
		matcher.setCriterion(traversingCriterion);

		StandardCloningContext cloningContext = new StandardCloningContext();

		cloningContext.setMatcher(matcher);
		cloningContext.setAbsenceResolvable(true);

		Object cloned = BaseType.INSTANCE.clone(cloningContext, result, StrategyOnCriterionMatch.partialize);

		return cloned;
	}

	class TagPrinter extends AbstractStringifier {

		private final GmTestTag tag;

		public TagPrinter(GmTestTag tag) {
			super();
			this.tag = tag;
		}

		public String print() {
			printTag(tag);

			return builder.toString();
		}

		private void printTag(GmTestTag tag) {
			if (tag == null)
				return;

			println("TAG: " + tag.getName());
			levelUp();
			{
				printTag(tag.getTag());
			}
			levelDown();

		}

	}

}