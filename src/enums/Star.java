package enums;

import java.util.Optional;

public enum Star {
	// Numbered comments denote the "radius" of the star.
	// The old system editor used a star's radius to derive its other attributes. I instead start with the habitable
	// range and derive anything else needed from that. Since some objects may be small and dense but be less habitable,
	// the habitable and mass values can be defined separately.
	// radius^3 / 25 = habitable
	// radius^3 / 4 = mass
	// radius = Math.pow(habitable * 25, 1./3.);
	// radius = Math.pow(mass * 4, 1./3.);

	O_SUPERGIANT("o-supergiant", 33450., 33450.), // 1.5x giant
	O_GIANT("o-giant", 22300., 22300.), // 1.5x above the gap between the next two smallest
	O_0("o0", 13720., 13720.), // 70
	O_3("o3", 11500., 11500.), // ~66
	O_5("o5", 10000., 10000.), // ~63
	O_8("o8", 8650., 8650.), // ~60
	O_DWARF("o-dwarf", 1325., 1325.), // Proportional with o8

	B_SUPERGIANT("b-supergiant", 17025., 17025.), // 1.5x giant
	B_GIANT("b-giant", 11350., 11350.), // 1.5x above the gap between the next two smallest
	B_0("b0", 7000., 7000.), // ~56
	B_3("b3", 6300., 6300.), // ~54
	B_5("b5", 5600., 5600.), // ~52
	B_8("b8", 5000., 5000.), // 50
	B_DWARF("b-dwarf", 1125., 1125.), // Proportional with b8

	A_SUPERGIANT("a-supergiant", 11850., 11850.), // 1.5x giant
	A_GIANT("a-giant", 7900., 7900.), // 1.5x above the gap between the next two smallest
	A_0("a0", 3650., 3650.), // ~45
	A_3("a3", 3400., 3400.), // ~44
	A_5("a5", 3200., 3200.), // ~43
	A_8("a8", 3000., 3000.), // ~42
	A_DWARF("a-dwarf", 750., 750.), //  Proportional with a8

	F_SUPERGIANT("f-supergiant", 8400., 8400.), // 1.5x giant
	F_GIANT("f-giant", 5600., 5600.), // 1.5x above the gap between the next two smallest
	F_0("f0", 2560., 2560.), // 40
	F_3("f3", 2200., 2200.), // 38
	F_5("f5", 1715., 1715.), // 35
	F_5_OLD("f5-old", 3430., 3430.), // 2x f5
	F_8("f8", 1310., 1310.), // 32
	F_DWARF("f-dwarf", 355., 355.), // Proportional with f8

	G_SUPERGIANT("g-supergiant", 6075., 6075.), // 1.5x giant
	G_GIANT("g-giant", 4050., 4050.), // 1.5x above the gap between k-giant and m-giant
	G_0("g0", 1080., 1080.), // 30
	G_0_OLD("g0-old", 2160., 2160.), // 2x g0
	G_3("g3", 700., 700.), // 26
	G_5("g5", 625., 625.), // 25
	G_5_OLD("g5-old", 1250., 1250.), // 2x g5
	G_8("g8", 550., 550.), // 24
	G_DWARF("g-dwarf", 150., 150.), // Proportional with g8

	K_SUPERGIANT("k-supergiant", 4500., 4500.), // 1.5x giant
	K_GIANT("k-giant", 3000., 3000.), // Proportional to k0
	K_0("k0", 490., 490.), // ~23
	K_0_OLD("k0-old", 980., 980.), // 2x k0
	K_3("k3", 450., 450.), // ~22.5
	K_5("k5", 425., 425.), // ~22
	K_5_OLD("k5-old", 950., 950.), // 2x k5
	K_8("k8", 370., 370.), // ~21
	K_DWARF("k-dwarf", 100., 100.), // Proportional with k8

	M_SUPERGIANT("m-supergiant", 3450., 3450.), // 1.5x giant
	M_GIANT("m-giant", 2300., 2300.), // Proportional to m0
	M_0("m0", 320., 320.), // ~20
	M_3("m3", 230., 230.), // ~18
	M_5("m5", 160., 160.), // ~16
	M_8("m8", 135., 135.), // 15
	M_DWARF("m-dwarf", 35., 35.), // Proportional with m8

	L_DWARF("l-dwarf", 30., 30.), // Less than m-dwarf
	
	CARBON("carbon", 3000., 3000.),
	NOVA("nova", 100., 5000.),
	NOVA_OLD("nova-old", 100., 5000.),
	NOVA_SMALL("nova-small", 100., 5000.),
	NEUTRON("neutron", 100., 5000.),
	WR("wr", 50000., 5000.),
	SUPER_MASSIVE_BLACK_HOLE("black-hole", 100000., 100000.),
	SMALL_BLACK_HOLE("small-black-hole", 10000., 10000.),
	COAL_BLACK_HOLE("coal-black-hole", 10000., 10000.),

	// Use a higher mass than habitable range so that object periods aren't super high.
	BROWN_DWARF_L("browndwarf-l-rogue", 10., 20.),
	BROWN_DWARF_T("browndwarf-t-rogue", 10., 20.),
	BROWN_DWARF_Y("browndwarf-y-rogue", 10., 20.),

	// Chopping block
	ROUGE_RADIATING("rogue-radiating", 10., 20.),
	PROTO_ORANGE("protostar-orange", 370., 370.),
	PROTO_YELLOW("protostar-yellow", 135., 135.),
	GIANT("giant", 3450., 3450.),

	// Default value for systems without a known star.
	DEFAULT("default", 100., 100.);

	private final String name;
	private final double habitable;
	private final double mass;

	Star(String name, double habitable, double mass) {
		this.name = name;
		this.habitable = habitable;
		this.mass = mass;
	}

	public double Habitable() {
		return habitable;
	}

	public double Mass() {
		// radius^3 / 25 = habitable
		// radius^3 / 4 = mass
		// mass * 6.25 = habitable
		// I use habitable as the base attribute of stars instead of radius.
		// I could just multiply the mass value of each enum by 6.25 in their definitions, but that would be work. :(
		return mass * 6.25;
	}

	public static Optional<Star> GetStar(String name) {
		for(Star info : Star.values())
			if(info.name.equals(name))
				return Optional.of(info);
		return Optional.empty();
	}
}
