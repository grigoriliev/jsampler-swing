open module com.grigoriliev.jsampler.swing {
	requires com.grigoriliev.jsampler;
	requires com.grigoriliev.jsampler.jlscp;
	requires com.grigoriliev.jsampler.juife;
	requires com.grigoriliev.jsampler.juife.swing;

	requires java.desktop;
	requires java.logging;

	exports com.grigoriliev.jsampler.swing.view;
	exports com.grigoriliev.jsampler.swing.view.std;
}