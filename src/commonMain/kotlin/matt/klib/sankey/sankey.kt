package matt.klib.sankey

import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import matt.klib.css.px
import matt.klib.css.sty

fun sankeyHTML(code: String): String {
  val h = createHTML()
  h.html {
	head {
	  script {
		type = "text/javascript"
		src = "https://www.gstatic.com/charts/loader.js"
	  }
	  script {
		type = "text/javascript"
		+sankeyJS(code)
	  }
	}
	body {
	  div {
		id = "sankey_basic"
		sty {
		  width = 900.px
		  height = 300.px
		}
	  }
	}
  }
  return h.finalize()
}

// language=JavaScript
fun sankeyJS(code: String) = """
     google.charts.matt.gui.ser.load('current', {'packages':['sankey']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'From');
          data.addColumn('string', 'To');
          data.addColumn('number', 'Weight');
          data.addRows([
//            [ 'A', 'X', 5.3 ],
//            [ 'A', 'Y', 7 ],
//            [ 'A', 'Z', 6 ],
//            [ 'B', 'X', 2 ],
//            [ 'B', 'Y', 9 ],
//            [ 'B', 'Z', 4 ],
            ${code}
          ]);

          // Sets chart options.
          var options = {
            width: 600,
          };

          // Instantiates and draws our chart, passing in some options.
          var chart = new google.visualization.Sankey(document.getElementById('sankey_basic'));
          chart.draw(data, options);
        }
""".trimIndent()
