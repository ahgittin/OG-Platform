/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap.pricing.vanilla.isdanew;

import static com.opengamma.financial.convention.businessday.BusinessDayDateUtils.addWorkDays;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import com.opengamma.analytics.financial.credit.StubType;
import com.opengamma.analytics.financial.credit.isdayieldcurve.ISDAInstrumentTypes;
import com.opengamma.analytics.financial.model.BumpType;
import com.opengamma.analytics.financial.schedule.NoHolidayCalendar;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.util.ArgumentChecker;

/**
 * 
 */
public class CS01Test {
  private static final SpreadSensitivityCalculator CS01_CAL = new SpreadSensitivityCalculator();

  private static final Calendar DEFAULT_CALENDAR = new MondayToFridayCalendar("Weekend_Only");
  // private static final Calendar DEFAULT_CALENDAR = new NoHolidayCalendar();
  private static final DayCount ACT365 = DayCountFactory.INSTANCE.getDayCount("ACT/365");
  private static final DayCount ACT360 = DayCountFactory.INSTANCE.getDayCount("ACT/360");
  private static final DayCount D30360 = DayCountFactory.INSTANCE.getDayCount("30/360");

  private static final BusinessDayConvention FOLLOWING = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Following");
  private static final BusinessDayConvention MOD_FOLLOWING = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Modified Following");

  private static final LocalDate TODAY = LocalDate.of(2013, 6, 4);
  private static final LocalDate EFFECTIVE_DATE = TODAY.plusDays(1); // AKA stepin date
  private static final LocalDate CASH_SETTLE_DATE = addWorkDays(TODAY, 3, DEFAULT_CALENDAR); // AKA valuation date
  private static final LocalDate STARTDATE = LocalDate.of(2013, 2, 2);
  private static final LocalDate[] MATURITIES = new LocalDate[] {LocalDate.of(2013, 6, 20), LocalDate.of(2013, 9, 20), LocalDate.of(2013, 12, 20), LocalDate.of(2014, 3, 20),
      LocalDate.of(2014, 6, 20), LocalDate.of(2014, 9, 20), LocalDate.of(2014, 12, 20), LocalDate.of(2015, 3, 20), LocalDate.of(2015, 6, 20), LocalDate.of(2015, 9, 20), LocalDate.of(2015, 12, 20),
      LocalDate.of(2016, 3, 20), LocalDate.of(2016, 6, 20), LocalDate.of(2016, 9, 20), LocalDate.of(2016, 12, 20), LocalDate.of(2017, 3, 20), LocalDate.of(2017, 6, 20), LocalDate.of(2017, 9, 20),
      LocalDate.of(2017, 12, 20), LocalDate.of(2018, 3, 20), LocalDate.of(2018, 6, 20), LocalDate.of(2018, 9, 20), LocalDate.of(2018, 12, 20), LocalDate.of(2019, 3, 20), LocalDate.of(2019, 6, 20),
      LocalDate.of(2019, 9, 20), LocalDate.of(2019, 12, 20), LocalDate.of(2020, 3, 20), LocalDate.of(2020, 6, 20), LocalDate.of(2020, 9, 20), LocalDate.of(2020, 12, 20), LocalDate.of(2021, 3, 20),
      LocalDate.of(2021, 6, 20), LocalDate.of(2021, 9, 20), LocalDate.of(2021, 12, 20), LocalDate.of(2022, 3, 20), LocalDate.of(2022, 6, 20), LocalDate.of(2022, 9, 20), LocalDate.of(2022, 12, 20),
      LocalDate.of(2023, 3, 20), LocalDate.of(2023, 6, 20)};

  private static final double[] PAR_SPREADS = new double[] {10.7, 13.17, 21.35, 30.63, 36.74, 44.49, 53.5, 61.4, 66.55, 70.79, 74.2, 74.2, 74.2, 74.2};

  private static final double[] FLAT_SPREADS = new double[] {8.97, 9.77, 10.7, 11.96, 13.17, 15.59, 17.8, 19.66, 21.35, 23.91, 26.54, 28.56, 30.63, 32.41, 34.08, 35.33, 36.74, 38.9, 40.88, 42.71,
      44.49, 46.92, 49.2, 51.36, 53.5, 55.58, 57.59, 59.49, 61.4, 62.76, 64.11, 65.35, 66.55, 67.58, 68.81, 69.81, 70.79, 71.65, 72.58, 73.58, 74.2};

  // These numbers come from The ISDA excel plugin
  private static final double[] PARELLEL_CS01_FLAT = new double[] {4.44388460893843, 30.033640328983, 55.3853989749605, 80.4665679983788, 106.113611507615, 131.76855171026, 157.157114109902,
      182.279368810202, 207.956446565041, 233.488342547238, 258.618042600828, 283.695469500717, 308.916298196751, 334.008749654446, 358.675305840658, 382.994684077393, 407.64905916453,
      431.852151546102, 455.57043360692, 478.808476009465, 502.318585908348, 525.220273167086, 547.569648624322, 569.368808982763, 591.319501611551, 612.909785294567, 633.906026965003,
      654.590133161596, 675.110290122106, 695.67623150317, 715.659552150048, 735.161267994736, 754.81254765758, 774.280598999456, 792.972762570697, 811.353868713068, 829.858325115361,
      848.167780637912, 865.852406503755, 882.910014941368, 900.572909625516};

  private static final double[] PARELLEL_CS01_TS = new double[] {4.44270353942497, 30.0278505776398, 55.3868828464654, 80.4734104513718, 106.127260703191, 131.794539837356, 157.209927186215,
      182.372358231722, 208.12006951847, 233.7275289487, 258.987409407063, 284.178525610287, 309.575849697874, 334.809534792469, 359.644785347887, 384.086200723836, 408.946037869194,
      433.442466121137, 457.469236295568, 481.034666114259, 504.921733027089, 528.313650464244, 551.178748198784, 573.529094140796, 596.107540537752, 618.263785438271, 639.874499893493,
      661.186271106971, 682.431640940034, 703.467480283569, 723.983498025077, 743.993186594343, 764.162951254262, 784.014616982583, 803.361059556938, 822.215898645791, 841.207351388533,
      859.917029630126, 878.142241147122, 895.896360249962, 913.770089165433};

  private static final double[][] BUCKETED_CS01_FLAT = new double[][] {
      {4.44275669542324, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {30.0292310963296, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {55.3868828464654, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {28.2168461525276, 52.2591747114526, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.419986404378084, 105.700057807618, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.40710101592567, 79.4106031728071, 51.965757893048, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.39524299172769, 53.3359988692059, 103.444251990699, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.385103854857322, 27.4962192421332, 154.415044508374, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.375772666229984, 1.03952603476565, 206.560705835027, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.362195456835024, 1.0029496921235, 155.167320932584, 77.0027594144858, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.348308455723478, 0.965438932753332, 104.344793660723, 153.020398416158, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.337419317535115, 0.936140150557818, 53.6022607828049, 228.881332393738, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.326300478085267, 0.906159887771596, 2.37638560744366, 305.362165353675, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.316619326437562, 0.880103549417283, 2.30697476944972, 228.800105351902, 101.811912697064, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.30751147132857, 0.855579898796643, 2.24164079537861, 153.309056168012, 202.092704070483, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.300471730815821, 0.836741745016689, 2.19120535705636, 78.933117539641, 300.861694691366, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.292663938715465, 0.815751593826602, 2.13518302589685, 3.17521579367916, 401.33617326029, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.281289593565687, 0.784807105101704, 2.05328307813918, 3.04799330208549, 300.180769637337, 125.693189995439, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.270844686487554, 0.756382908525394, 1.97803401839197, 2.93107551149058, 200.705603209946, 249.154127416648, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.261180249577081, 0.730072868309478, 1.90837060650567, 2.82283902493935, 102.925801921998, 370.377571447027, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.251787019359129, 0.704482806122148, 1.84062138396113, 2.71766640087129, 3.62286318756483, 493.351427038571, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.239386707651112, 0.670449115634797, 1.75096484996903, 2.58070903820171, 3.43500112475609, 368.365451232078, 148.469048295494, 0, 0, 0, 0, 0, 0, 0},
      {0.227791715504133, 0.638593796911668, 1.66706396278679, 2.4527102489777, 3.25960725556032, 245.746001006103, 293.918510426105, 0, 0, 0, 0, 0, 0, 0},
      {0.216854533340721, 0.608512239486103, 1.58785976536041, 2.33207196656537, 3.09451337750233, 125.505620909457, 436.346668532012, 0, 0, 0, 0, 0, 0, 0},
      {0.206079508669604, 0.578838076897153, 1.50976478922871, 2.21337127900498, 2.93234639231477, 3.69871733817834, 580.424395217643, 0, 0, 0, 0, 0, 0, 0},
      {0.195672510666656, 0.550138315819071, 1.43427360704135, 2.09888676754033, 2.7762277134899, 3.49708608032157, 433.176400358885, 169.588706987196, 0, 0, 0, 0, 0, 0},
      {0.185686052922573, 0.522558909851756, 1.36177161932233, 1.98920322624918, 2.62695605411645, 3.30460384377618, 288.981421040571, 335.40592130283, 0, 0, 0, 0, 0, 0},
      {0.17629007625225, 0.496586120030118, 1.29350935523798, 1.88606917724682, 2.48674697526929, 3.12395521570585, 146.293928095009, 499.275645178793, 0, 0, 0, 0, 0, 0},
      {0.166929044975728, 0.470666465919589, 1.22544067716196, 1.78354477896325, 2.34772585669396, 2.9452078520939, 3.59479635291676, 662.901188698849, 0, 0, 0, 0, 0, 0},
      {0.160124679071671, 0.451895553706116, 1.17599484551062, 1.7083721335423, 2.24498154734187, 2.81223483850762, 3.42852916767472, 493.945168638647, 190.290079229972, 0, 0, 0, 0, 0},
      {0.153433158081584, 0.433403804400712, 1.12733098057982, 1.63464240063305, 2.14449714009551, 2.682484712975, 3.26657231688315, 328.626408782007, 376.215656638007, 0, 0, 0, 0, 0},
      {0.14729718848111, 0.416441417454283, 1.08269009983786, 1.56702404038173, 2.05235736740889, 2.56352147722655, 3.1180842657505, 166.945071880191, 557.852222912963, 0, 0, 0, 0, 0},
      {0.141379918855433, 0.400072916136029, 1.0396207593133, 1.50185031796285, 1.96361982831433, 2.44902188208107, 2.9752299902791, 3.54678616468901, 741.209707486222, 0, 0, 0, 0, 0},
      {0.136262781759433, 0.385935441007956, 1.00237882200843, 1.44530399388137, 1.88640706079435, 2.3491548484314, 2.85039555217997, 3.39519167089075, 551.990589717222, 209.529602470913, 0, 0, 0, 0},
      {0.130327676241271, 0.369455056922763, 0.959112100150372, 1.38034493969014, 1.79854773060123, 2.23640057597785, 2.71030112871523, 3.22574059441472, 366.903034046337, 414.053506416767, 0, 0, 0,
          0},
      {0.125451561479306, 0.355939941257266, 0.923574619872414, 1.32673634858221, 1.72574351351568, 2.14264817649523, 2.59349978835172, 3.0841973819376, 186.077411391948, 613.733480971913, 0, 0, 0, 0},
      {0.120695531610759, 0.342747066436888, 0.888896827686003, 1.27449962238069, 1.6548871965838, 2.05149137262606, 2.48001273323722, 2.94672767932325, 3.44985047621288, 815.158828780443, 0, 0, 0, 0},
      {0.116498619286143, 0.331116062159131, 0.858297479408576, 1.22828721135781, 1.59206314555987, 1.97051788657782, 2.37905387087678, 2.82430573157744, 3.30482847203661, 606.753913155463,
          227.664412485055, 0, 0, 0},
      {0.112048626585426, 0.31874285805894, 0.82581626904138, 1.17958978262944, 1.52627109708398, 1.88615123395547, 2.27428062105073, 2.69758834506306, 3.15495004793442, 403.079420980251,
          449.778345152398, 0, 0, 0},
      {0.107350377517856, 0.305639610301278, 0.791486117410201, 1.12846426840452, 1.45759648435828, 1.79850963977213, 2.16584766038475, 2.56676785306831, 3.00044455388282, 204.100161117243,
          666.386020341983, 0, 0, 0},
      {0.104286288313271, 0.297165789434062, 0.7691460192083, 1.09452285262934, 1.41121769341168, 1.73847695020268, 2.09074267459863, 2.47547555579097, 2.8921185821762, 3.34477564953306,
          884.9662862076, 0, 0, 0}};

  private static final double[][] BUCKETED_CS01_TS = new double[][] {
      {4.44270353942497, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {30.0278505776398, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {55.3868828464654, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {28.2126202539729, 52.2656333140862, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.420049420366345, 105.712077958918, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.409085625416306, 79.3768750722273, 52.0237104432632, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.398231019398468, 53.2960183187939, 103.536394075164, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.387485995508724, 27.4707421277567, 154.535904512417, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.376492972027631, 1.04035512787926, 206.721491059769, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.364277365036414, 1.00736139383231, 155.160594792942, 77.2425316789664, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.352228056648557, 0.974815752199237, 104.290175365007, 153.432646045761, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.340212021811281, 0.94235893839667, 53.5489984024411, 229.411372018457, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.328098313427672, 0.909637013414566, 2.38253795959917, 306.008595441942, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.319762723039602, 0.88721773347164, 2.32253315082115, 229.137926119657, 102.248465369641, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.311559263013739, 0.865153077941883, 2.26347897864729, 153.463743563974, 202.872911301638, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.3034863896266, 0.843438929438306, 2.20536430145871, 78.9728057994515, 301.892390060145, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.295276146297652, 0.821354257728862, 2.14625994265569, 3.18796793736739, 402.599069654102, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.285112892458445, 0.793683071133311, 2.07284279080122, 3.07345914933935, 300.807969676277, 126.597901243024, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.27514423106903, 0.766541525774878, 2.00083119574271, 2.961144258469, 200.944649514492, 250.747423838078, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.265366723616045, 0.739920269485217, 1.9302003276718, 2.85098434237019, 102.975735834621, 372.492613021757, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.255455757783296, 0.712935210221699, 1.85860504301957, 2.73932424164902, 3.64386067406089, 495.880867067471, 0, 0, 0, 0, 0, 0, 0, 0},
      {0.243987042066601, 0.681421926090192, 1.77553866343924, 2.6124023510439, 3.46973254425689, 369.681438878295, 150.141811956609, 0, 0, 0, 0, 0, 0, 0},
      {0.232776106105781, 0.650616991358655, 1.69433936206276, 2.48833295664677, 3.29951765460149, 246.290567331695, 296.867506653208, 0, 0, 0, 0, 0, 0, 0},
      {0.221817061215462, 0.620504224297519, 1.61496448859016, 2.36705090380165, 3.13312645094238, 125.645012403336, 440.2564732977, 0, 0, 0, 0, 0, 0, 0},
      {0.210745866036088, 0.590083063054664, 1.53477714002342, 2.24452952777079, 2.96503737079803, 3.7317284192484, 585.078129887007, 0, 0, 0, 0, 0, 0, 0},
      {0.20082850085007, 0.562715840542183, 1.46285824423331, 2.13570681230346, 2.8169523851318, 3.54065487613575, 435.920629882597, 172.040720662965, 0, 0, 0, 0, 0, 0},
      {0.191154935200166, 0.536021335983961, 1.39270728945207, 2.02955969084984, 2.67250885793019, 3.35428070324245, 290.386436035323, 339.79794705246, 0, 0, 0, 0, 0, 0},
      {0.181614841776589, 0.509695057819559, 1.32352417134274, 1.92487787248341, 2.5300602626982, 3.17048158748195, 146.816313218001, 505.188723154838, 0, 0, 0, 0, 0, 0},
      {0.172104374286663, 0.483450141648689, 1.25455558822735, 1.82052422473267, 2.38806231277894, 2.98726822234308, 3.63881896457696, 670.023591486532, 0, 0, 0, 0, 0, 0},
      {0.165550528535841, 0.465377721442506, 1.20703826171631, 1.748504847493, 2.28992187773641, 2.86049371280711, 3.48040464839827, 498.602123988508, 193.209595676154, 0, 0, 0, 0, 0},
      {0.159158447129992, 0.447751293944365, 1.16069371049188, 1.67826366986867, 2.19420537444365, 2.73685118312716, 3.32590477062494, 331.349011612664, 381.580604663208, 0, 0, 0, 0, 0},
      {0.1529239381029, 0.430559305035494, 1.115491549665, 1.60975460597457, 2.10084996419418, 2.61625942148819, 3.17521781297708, 168.158291720462, 565.240820763466, 0, 0, 0, 0, 0},
      {0.146639285832084, 0.413229037043994, 1.06992582603327, 1.54069457339312, 2.00674382306448, 2.4946979635676, 3.02331921294557, 3.59750818332977, 750.305883072622, 0, 0, 0, 0, 0},
      {0.141288166395637, 0.398435711021494, 1.03110013004171, 1.48219058450361, 1.92741264735952, 2.39263278414836, 2.89617822375743, 3.4433815646745, 558.014874206557, 213.010509520807, 0, 0, 0, 0},
      {0.136073134343606, 0.384018500991512, 0.99326175302672, 1.42517524573393, 1.85010113627759, 2.29316716630207, 2.77227654844892, 3.29318266452383, 370.528429416171, 420.517091874351, 0, 0, 0, 0},
      {0.130990525752661, 0.369967283253558, 0.956384114562947, 1.36960845698031, 1.77475485964307, 2.19623102392513, 2.65152682595721, 3.14680553503005, 187.719990606766, 622.671000121366, 0, 0, 0,
          0},
      {0.125870883389834, 0.355813668237448, 0.919237757260327, 1.31363689061942, 1.69885986422286, 2.09858908154642, 2.52989805725468, 2.9993629038147, 3.50256435911744, 826.205942562788, 0, 0, 0, 0},
      {0.121592273479021, 0.343963300300509, 0.888177032344617, 1.26703438302345, 1.63589867956809, 2.01782985907034, 2.42953440551114, 2.87788680081863, 3.35889040401632, 614.220225571366,
          231.657836866565, 0, 0, 0},
      {0.117424292879476, 0.3324193223464, 0.857919410068853, 1.22163698248329, 1.57456580785614, 1.93915943743489, 2.33176679878544, 2.75955299069292, 3.21893348820962, 407.613956328538,
          457.208549452769, 0, 0, 0},
      {0.113363892439988, 0.32117329029635, 0.828442755229641, 1.17741146385408, 1.51481634366735, 1.86252019980515, 2.23652361688864, 2.64427477417173, 3.08259060660676, 206.242346351429,
          676.824474037663, 0, 0, 0},
      {0.109276238918765, 0.30985157893626, 0.798768104201542, 1.13289067206479, 1.45467003437316, 1.78537413728086, 2.14065271197939, 2.52823849759831, 2.94535236432247, 3.39918437135267,
          897.821618556768, 0, 0, 0}};

  private static final double DEAL_SPREAD = 100.0;
  private static final boolean PAY_ACC_ON_DEFAULT = true;
  private static final Period TENOR = Period.ofMonths(3);
  private static final StubType STUB = StubType.FRONTSHORT;
  private static final boolean PROCTECTION_START = true;

  private static final LocalDate[] PAR_SPREAD_DATES = new LocalDate[] {LocalDate.of(2013, 12, 20), LocalDate.of(2014, 6, 20), LocalDate.of(2015, 6, 20), LocalDate.of(2016, 6, 20),
      LocalDate.of(2017, 6, 20), LocalDate.of(2018, 6, 20), LocalDate.of(2019, 6, 20), LocalDate.of(2020, 6, 20), LocalDate.of(2021, 6, 20), LocalDate.of(2022, 6, 20), LocalDate.of(2023, 6, 20),
      LocalDate.of(2028, 6, 20), LocalDate.of(2033, 6, 20), LocalDate.of(2043, 6, 20)};

  private static final double RECOVERY_RATE = 0.4;
  private static final double NOTIONAL = 1e6;

  // yield curve
  private static final LocalDate SPOT_DATE = LocalDate.of(2013, 6, 6);
  private static final ISDACompliantYieldCurveBuild YIELD_CURVE_BUILDER = new ISDACompliantYieldCurveBuild();
  private static final ISDACompliantYieldCurve YIELD_CURVE;

  static {
    final int nMoneyMarket = 5;
    final int nSwaps = 14;
    final int nInstruments = nMoneyMarket + nSwaps;

    final ISDAInstrumentTypes[] types = new ISDAInstrumentTypes[nInstruments];
    Period[] tenors = new Period[nInstruments];
    final int[] mmMonths = new int[] {1, 2, 3, 6, 12};
    final int[] swapYears = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 20, 25, 30};
    // check
    ArgumentChecker.isTrue(mmMonths.length == nMoneyMarket, "mmMonths");
    ArgumentChecker.isTrue(swapYears.length == nSwaps, "swapYears");

    for (int i = 0; i < nMoneyMarket; i++) {
      types[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors[i] = Period.ofMonths(mmMonths[i]);
    }
    for (int i = nMoneyMarket; i < nInstruments; i++) {
      types[i] = ISDAInstrumentTypes.Swap;
      tenors[i] = Period.ofYears(swapYears[i - nMoneyMarket]);
    }

    final double[] rates = new double[] {0.00194, 0.002292, 0.002733, 0.004153, 0.006902, 0.004575, 0.006585, 0.00929, 0.012175, 0.0149, 0.01745, 0.019595, 0.02144, 0.023045, 0.02567, 0.02825,
        0.03041, 0.031425, 0.03202};

    final DayCount moneyMarketDCC = ACT360;
    final DayCount swapDCC = D30360;
    final DayCount curveDCC = ACT365;
    final Period swapInterval = Period.ofMonths(6);

    YIELD_CURVE = YIELD_CURVE_BUILDER.build(TODAY, SPOT_DATE, types, tenors, rates, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);

    // YIELD_CURVE = new ISDACompliantYieldCurve(1.0, 0.05);
  }

  @Test
  public void debugTest() {
    final FastCreditCurveBuilder bob = new FastCreditCurveBuilder();
    final PointsUpFrontConverter pufConverter = new PointsUpFrontConverter();

    final int m = PAR_SPREAD_DATES.length;
    final double[] parSpreads = new double[m];
    final CDSAnalytic[] curveCDSs = new CDSAnalytic[m];
    for (int i = 0; i < m; i++) {
      curveCDSs[i] = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, TODAY, PAR_SPREAD_DATES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
      parSpreads[i] = PAR_SPREADS[m - 1] / 10000;
    }
    ISDACompliantCreditCurve curve1 = bob.calibrateCreditCurve(curveCDSs, parSpreads, YIELD_CURVE);

    double[] quotedSpreads = pufConverter.parSpreadsToQuotedSpreads(curveCDSs, 0.01, YIELD_CURVE, parSpreads);
    double[] debug = pufConverter.quotedSpreadToParSpreads(curveCDSs, 0.01, YIELD_CURVE, parSpreads);
    // double[] puf = pufConverter.quotedSpreadsToPUF(curveCDSs, 0.01, YIELD_CURVE, quotedSpreads);

    ISDACompliantCreditCurve curve2 = bob.calibrateCreditCurve(curveCDSs, quotedSpreads, YIELD_CURVE);

    for (int i = 0; i < m; i++) {
      ISDACompliantCreditCurve curve3 = bob.calibrateCreditCurve(curveCDSs[i], quotedSpreads[i], YIELD_CURVE);
      ISDACompliantCreditCurve curve4 = bob.calibrateCreditCurve(curveCDSs[i], parSpreads[i], YIELD_CURVE);
      ISDACompliantCreditCurve curve5 = bob.calibrateCreditCurve(curveCDSs[i], debug[i], YIELD_CURVE);

      final double t = ACT365.getDayCountFraction(TODAY, PAR_SPREAD_DATES[i]);
      final double dp1 = 1 - curve1.getSurvivalProbability(t);
      final double dp2 = 1 - curve2.getSurvivalProbability(t);
      final double dp3 = 1 - curve3.getSurvivalProbability(t);
      final double dp4 = 1 - curve4.getSurvivalProbability(t);
      final double dp5 = 1 - curve5.getSurvivalProbability(t);
      System.out.println(PAR_SPREAD_DATES[i] + "\t" + t + "\t" + dp1 * 100 + "%\t" + dp2 * 100 + "%\t" + dp3 * 100 + "%\t" + dp4 * 100 + "%\t" + dp5 * 100 + "%");
    }
  }

  @Test
  public void parellelCS01FromQuotedSpreadsTest() {
    final double bumpAmount = 1e-4; // 1pb

    final double coupon = DEAL_SPREAD / 10000;
    final double scale = NOTIONAL / 10000;

    final int n = MATURITIES.length;
    for (int i = 0; i < n; i++) {
      final CDSAnalytic cds = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, STARTDATE, MATURITIES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
      double cs01 = scale * CS01_CAL.parallelCS01FromQuotedSpread(cds, coupon, YIELD_CURVE, FLAT_SPREADS[i] / 10000, bumpAmount, BumpType.ADDITIVE);
      assertEquals(MATURITIES[i].toString(), PARELLEL_CS01_FLAT[i], cs01, 1e-14 * NOTIONAL);
    //  System.out.println(cs01);
    }
  }

  @Test
  public void parellelCS01FlatTest() {
    final double bumpAmount = 1e-4; // 1pb

    final int m = PAR_SPREAD_DATES.length;
    final CDSAnalytic[] curveCDSs = new CDSAnalytic[m];
    for (int i = 0; i < m; i++) {
      curveCDSs[i] = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, TODAY, PAR_SPREAD_DATES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
    }

    final double fracSpread = DEAL_SPREAD / 10000;
    final double scale = NOTIONAL / 10000;

    final int n = MATURITIES.length;
    for (int i = 0; i < n; i++) {
      final CDSAnalytic cds = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, STARTDATE, MATURITIES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
      final double[] flatSpreads = new double[m];
      Arrays.fill(flatSpreads, FLAT_SPREADS[i] / 10000);
      final double temp = FLAT_SPREADS[i] / 10000;
      final double[] quotedSpread = new double[] {temp};
      double cs01 = scale * CS01_CAL.parallelCS01FromParSpreads(cds, fracSpread, YIELD_CURVE, curveCDSs, flatSpreads, bumpAmount, BumpType.ADDITIVE);
      // System.out.println(MATURITIES[i].toString() + "\t" + cs01);
      assertEquals(MATURITIES[i].toString(), PARELLEL_CS01_FLAT[i], cs01, 1e-14 * NOTIONAL);

      // debug
      double debug1 = scale * CS01_CAL.parallelCS01FromParSpreads(cds, fracSpread, YIELD_CURVE, new CDSAnalytic[] {cds}, quotedSpread, bumpAmount, BumpType.ADDITIVE);
      System.out.println(debug1);
    }
  }

  @Test
  public void parellelCS01TermStructureTest() {
    final double bumpAmount = 1e-4; // 1pb

    final int m = PAR_SPREAD_DATES.length;
    final double[] parSpreads = new double[m];
    final CDSAnalytic[] curveCDSs = new CDSAnalytic[m];
    for (int i = 0; i < m; i++) {
      parSpreads[i] = PAR_SPREADS[i] / 10000;
      curveCDSs[i] = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, TODAY, PAR_SPREAD_DATES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
    }

    final double fracSpread = DEAL_SPREAD / 10000;
    final double scale = NOTIONAL / 10000;

    final int n = MATURITIES.length;
    for (int i = 0; i < n; i++) {
      final CDSAnalytic cds = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, STARTDATE, MATURITIES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);

      double cs01 = scale * CS01_CAL.parallelCS01FromParSpreads(cds, fracSpread, YIELD_CURVE, curveCDSs, parSpreads, bumpAmount, BumpType.ADDITIVE);
      // System.out.println(MATURITIES[i].toString() + "\t" + cs01);
      assertEquals(MATURITIES[i].toString(), PARELLEL_CS01_TS[i], cs01, 1e-14 * NOTIONAL);
    }
  }

  @Test
  public void bucketedCS01FlatTest() {
    final double bumpAmount = 1e-4; // 1pb

    final int m = PAR_SPREAD_DATES.length;
    final CDSAnalytic[] curveCDSs = new CDSAnalytic[m];
    for (int i = 0; i < m; i++) {
      curveCDSs[i] = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, TODAY, PAR_SPREAD_DATES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
    }

    final double fracSpread = DEAL_SPREAD / 10000;
    final double scale = NOTIONAL / 10000;

    final int n = MATURITIES.length;
    for (int i = 0; i < n; i++) {
      final CDSAnalytic cds = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, STARTDATE, MATURITIES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
      final double[] flatSpreads = new double[m];
      Arrays.fill(flatSpreads, FLAT_SPREADS[i] / 10000);
      double[] cs01 = CS01_CAL.bucketedCS01FromParSpreads(cds, fracSpread, YIELD_CURVE, curveCDSs, flatSpreads, bumpAmount, BumpType.ADDITIVE);

      for (int j = 0; j < m; j++) {
        cs01[j] *= scale;
        assertEquals(MATURITIES[i].toString() + "\t" + PAR_SPREAD_DATES[j], BUCKETED_CS01_FLAT[i][j], cs01[j], 1e-13 * NOTIONAL);
      }
    }
  }

  @Test
  public void bucketedCS01TermStructureTest() {
    final double bumpAmount = 1e-4; // 1pb

    final int m = PAR_SPREAD_DATES.length;
    final double[] parSpreads = new double[m];
    final CDSAnalytic[] curveCDSs = new CDSAnalytic[m];
    for (int i = 0; i < m; i++) {
      parSpreads[i] = PAR_SPREADS[i] / 10000;
      curveCDSs[i] = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, TODAY, PAR_SPREAD_DATES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);
    }

    final double fracSpread = DEAL_SPREAD / 10000;
    final double scale = NOTIONAL / 10000;

    final int n = MATURITIES.length;
    for (int i = 0; i < n; i++) {
      final CDSAnalytic cds = new CDSAnalytic(TODAY, EFFECTIVE_DATE, CASH_SETTLE_DATE, STARTDATE, MATURITIES[i], PAY_ACC_ON_DEFAULT, TENOR, STUB, PROCTECTION_START, RECOVERY_RATE);

      double[] cs01 = CS01_CAL.bucketedCS01FromParSpreads(cds, fracSpread, YIELD_CURVE, curveCDSs, parSpreads, bumpAmount, BumpType.ADDITIVE);

      for (int j = 0; j < m; j++) {
        cs01[j] *= scale;
        assertEquals(MATURITIES[i].toString() + "\t" + PAR_SPREAD_DATES[j], BUCKETED_CS01_TS[i][j], cs01[j], 1e-13 * NOTIONAL);
      }
    }
  }
}
