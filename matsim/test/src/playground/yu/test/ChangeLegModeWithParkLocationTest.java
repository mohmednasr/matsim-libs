/**
 * 
 */
package playground.yu.test;

import org.matsim.core.api.experimental.ScenarioLoader;
import org.matsim.core.api.experimental.population.PlanElement;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.replanning.PlanStrategy;
import org.matsim.core.replanning.StrategyManager;
import org.matsim.core.replanning.StrategyManagerConfigLoader;
import org.matsim.core.replanning.modules.ReRoute;
import org.matsim.core.replanning.selectors.RandomPlanSelector;
import org.matsim.testcases.MatsimTestCase;

import playground.yu.scoring.CharyparNagelScoringFunctionFactoryWithWalk;

/**
 * @author yu
 * 
 */
public class ChangeLegModeWithParkLocationTest extends MatsimTestCase {
	private static class LegChainModesListener1 implements
			IterationEndsListener {
		public void notifyIterationEnds(IterationEndsEvent event) {
			Controler ctl = event.getControler();
			int itr = event.getIteration();
			String criterion = "";
			switch (itr) {
			case 0:
				criterion = "|walk|walk|walk|walk|walk|walk|walk|walk|";
				break;
			case 1:
				criterion = "|pt|walk|walk|walk|walk|walk|walk|walk|";
				break;
			case 2:
				criterion = "|car|walk|walk|walk|walk|walk|car|car|";
				break;
			case 3:
				criterion = "|car|walk|walk|walk|car|car|car|car|";
				break;
			case 4:
				criterion = "|car|walk|walk|walk|walk|walk|car|car|";
				break;
			case 5:
				criterion = "|walk|walk|walk|walk|walk|walk|walk|walk|";
				break;
			case 6:
				criterion = "|car|walk|walk|walk|walk|walk|car|car|";
				break;
			case 7:
				criterion = "|pt|walk|walk|walk|walk|walk|walk|pt|";
				break;
			case 8:
				criterion = "|walk|pt|pt|pt|pt|walk|walk|walk|";
				break;
			case 9:
				criterion = "|car|walk|pt|walk|car|car|car|car|";
				break;
			case 10:
				criterion = "|car|walk|pt|walk|walk|pt|car|car|";
				break;
			}
			System.out.println("----->currentIteration=" + itr);

			PlanImpl plan = ctl.getPopulation().getPersons().values().iterator()
					.next().getSelectedPlan();
			StringBuilder legChainModes = new StringBuilder("|");
			for (PlanElement pe : plan.getPlanElements())
				if (pe instanceof LegImpl)
					legChainModes.append(((LegImpl) pe).getMode() + "|");

			assertEquals("different legChainModes?", criterion, legChainModes
					.toString());
		}
	}

	private static class LegChainModesListener2 implements
			IterationEndsListener {
		public void notifyIterationEnds(IterationEndsEvent event) {
			Controler ctl = event.getControler();
			int itr = event.getIteration();
			String criterion = "";
			switch (itr) {
			case 0:
				criterion = "|car|car|car|car|car|car|car|car|";
				break;
			case 1:
				criterion = "|car|car|car|car|pt|walk|car|car|";
				break;
			case 2:
				criterion = "|car|pt|pt|walk|pt|walk|car|car|";
				break;
			case 3:
				criterion = "|car|pt|pt|walk|car|car|car|car|";
				break;
			case 4:
				criterion = "|walk|walk|pt|walk|pt|pt|walk|walk|";
				break;
			case 5:
				criterion = "|car|pt|pt|walk|pt|walk|car|car|";
				break;
			case 6:
				criterion = "|car|pt|pt|walk|car|car|car|car|";
				break;
			case 7:
				criterion = "|car|car|car|car|walk|walk|car|car|";
				break;
			case 8:
				criterion = "|walk|pt|pt|pt|pt|walk|walk|walk|";
				break;
			case 9:
				criterion = "|car|pt|walk|walk|car|car|car|car|";
				break;
			case 10:
				criterion = "|walk|pt|pt|pt|pt|walk|walk|walk|";
				break;
			}
			System.out.println("----->currentIteration=" + itr);

			PlanImpl plan = ctl.getPopulation().getPersons().values().iterator()
					.next().getSelectedPlan();
			StringBuilder legChainModes = new StringBuilder("|");
			for (PlanElement pe : plan.getPlanElements())
				if (pe instanceof LegImpl)
					legChainModes.append(((LegImpl) pe).getMode() + "|");

			assertEquals("different legChainModes?", criterion, legChainModes
					.toString());
		}
	}

	private static class LegChainModesListener3 implements
			IterationEndsListener {
		public void notifyIterationEnds(IterationEndsEvent event) {
			Controler ctl = event.getControler();
			int itr = event.getIteration();
			String criterion = "";
			switch (itr) {
			case 0:
				criterion = "|pt|pt|pt|pt|pt|pt|pt|pt|";
				break;
			case 1:
				criterion = "|walk|pt|pt|pt|pt|pt|pt|pt|";
				break;
			case 2:
				criterion = "|walk|pt|pt|walk|pt|pt|pt|pt|";
				break;
			case 3:
				criterion = "|walk|pt|pt|walk|pt|walk|pt|pt|";
				break;
			case 4:
				criterion = "|car|pt|pt|pt|pt|pt|car|car|";
				break;
			case 5:
				criterion = "|pt|pt|pt|pt|pt|pt|pt|pt|";
				break;
			case 6:
				criterion = "|pt|pt|pt|pt|pt|walk|pt|pt|";
				break;
			case 7:
				criterion = "|pt|pt|pt|pt|pt|pt|pt|walk|";
				break;
			case 8:
				criterion = "|walk|pt|pt|pt|pt|walk|pt|pt|";
				break;
			case 9:
				criterion = "|pt|pt|walk|pt|pt|pt|pt|pt|";
				break;
			case 10:
				criterion = "|walk|pt|pt|pt|pt|walk|walk|walk|";
				break;
			}
			System.out.println("----->currentIteration=" + itr);

			PlanImpl plan = ctl.getPopulation().getPersons().values().iterator()
					.next().getSelectedPlan();
			StringBuilder legChainModes = new StringBuilder("|");
			for (PlanElement pe : plan.getPlanElements())
				if (pe instanceof LegImpl)
					legChainModes.append(((LegImpl) pe).getMode() + "|");

			assertEquals("different legChainModes?", criterion, legChainModes
					.toString());
		}
	}

	// @Override
	// protected void tearDown() throws Exception {
	// super.tearDown();
	// Gbl.reset();
	// }

	public void testLegChainModes1() {
		// the agent in the initial plan has only "walk" legs.
		String[] args = new String[] { getInputDirectory() + "config1.xml" };
		Controler ctl = new ChangeLegModeWithParkLocationControler(args);
		ctl.addControlerListener(new LegChainModesListener1());
		ctl.setCreateGraphs(false);
		ctl.setWriteEventsInterval(0);
		ctl
				.setScoringFunctionFactory(new CharyparNagelScoringFunctionFactoryWithWalk(
						new ScenarioLoader(args[0]).loadScenario().getConfig()
								.charyparNagelScoring()));
		ctl.run();
	}

	public void testLegChainModes2() {
		// the agent in the initial plan has only "car" legs.
		String[] args = new String[] { getInputDirectory() + "config2.xml" };
		Controler ctl = new ChangeLegModeWithParkLocationControler(args);
		ctl.addControlerListener(new LegChainModesListener2());
		ctl.setCreateGraphs(false);
		ctl.setWriteEventsInterval(0);
		ctl
				.setScoringFunctionFactory(new CharyparNagelScoringFunctionFactoryWithWalk(
						new ScenarioLoader(args[0]).loadScenario().getConfig()
								.charyparNagelScoring()));
		ctl.run();
	}

	public void testLegChainModes3() {
		// the agent in the initial plan has only "pt" legs.
		String[] args = new String[] { getInputDirectory() + "config3.xml" };
		Controler ctl = new ChangeLegModeWithParkLocationControler(args);
		ctl.addControlerListener(new LegChainModesListener3());
		ctl.setCreateGraphs(false);
		ctl.setWriteEventsInterval(0);
		ctl
				.setScoringFunctionFactory(new CharyparNagelScoringFunctionFactoryWithWalk(
						new ScenarioLoader(args[0]).loadScenario().getConfig()
								.charyparNagelScoring()));
		ctl.run();
	}

	private static class ChangeLegModeWithParkLocationControler extends
			Controler {
		public ChangeLegModeWithParkLocationControler(String[] args) {
			super(args);
		}

		protected StrategyManager loadStrategyManager() {
			StrategyManager manager = new StrategyManager();
			StrategyManagerConfigLoader.load(this, this.config, manager);
			manager.setMaxPlansPerAgent(5);
			//
			// PlanStrategy strategy1 = new PlanStrategy(new
			// ExpBetaPlanChanger());
			// manager.addStrategy(strategy1, 0.1);

			PlanStrategy strategy2 = new PlanStrategy(new RandomPlanSelector());
			strategy2.addStrategyModule(new ChangeLegModeWithParkLocation(
					this.config));
			strategy2.addStrategyModule(new ReRoute(this));
			manager.addStrategy(strategy2, 0.5);

			// PlanStrategy strategy3 = new PlanStrategy(new
			// RandomPlanSelector());
			// strategy3.addStrategyModule(new ReRoute(this));
			// manager.addStrategy(strategy3, 0.4);

			return manager;
		}
	}
}
