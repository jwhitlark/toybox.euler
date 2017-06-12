(defproject euler "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha4"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/algo.generic "0.1.2"]
                 [org.flatland/useful "0.11.2"]
                 [org.clojure/math.combinatorics "0.0.8"]
                 [org.clojars.achim/multiset "0.1.0-SNAPSHOT"]
                 [net.mikera/core.matrix "0.32.1"]
                 [clatrix "0.4.0"]
                 [incanter "1.5.6"]
                 [clj-http "0.9.2"]]
  :main ^:skip-aot euler.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev"]
                   :plugins []
                   :dependencies [[org.clojure/tools.namespace "0.2.5"]
                                  [org.clojure/java.classpath "0.2.2"]
                                  [criterium "0.4.3"]
                                  [midje "1.6.3" :exclusions [org.codehaus.plexus/plexus-utils]]]}
}
)

"When asked to prove something true or false, people tend to focus on
confirming the rule rather than falsifying it. This is why they turn
over the 8 card, to confirm the rule by observing a vowel on the other
side, and the A card, to find the confirming even number. But if they
thought scientifically, they would look for a way to falsify the
rule—a thought pattern that would immediately suggest the relevance of
the 5 card (which might contain a disconfirming vowel on the
back). Seeking falsifying evidence is a crucial component of
scientific thinking. But for most people, this bit of mindware must be
taught until it becomes second nature."
