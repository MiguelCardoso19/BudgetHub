/**
 * User credentials for registration
 */
export interface UserCredentialsDto {

  /**
   * Date of birth of the user in YYYY-MM-DD format
   */
  dateOfBirth: string;

  /**
   * Email address of the user
   */
  email: string;

  /**
   * First name of the user
   */
  firstName: string;

  /**
   * Gender of the user
   */
  gender: 'Male' | 'Female';

  /**
   * Unique identifier (UUID) for each entity instance
   */
  id?: string;

  /**
   * Last name of the user
   */
  lastName: string;

  /**
   * Nationality of the user
   */
  nationality: 'Afghan' | 'Albanian' | 'Algerian' | 'Andorran' | 'Angolan' | 'Argentine' | 'Armenian' | 'Australian' | 'Austrian' | 'Azerbaijani' | 'Bahraini' | 'Bangladeshi' | 'Belarusian' | 'Belgian' | 'Belizean' | 'Beninese' | 'Bhutanese' | 'Bolivian' | 'Bosnian' | 'Botswanan' | 'Brazilian' | 'Bruneian' | 'Bulgarian' | 'Burkinabe' | 'Burundian' | 'Cambodian' | 'Cameroonian' | 'Canadian' | 'Capeverdean' | 'Centralafrican' | 'Chadian' | 'Chilean' | 'Chinese' | 'Colombian' | 'Comorian' | 'Congolese' | 'CongoleseDRC' | 'Costarican' | 'Ivorian' | 'Croatian' | 'Cuban' | 'Cypriot' | 'Czech' | 'Danish' | 'Djiboutian' | 'Dominican' | 'DominicanRep' | 'Ecuadorian' | 'Egyptian' | 'Salvadoran' | 'Equatorialguinean' | 'Eritrean' | 'Estonian' | 'Ethiopian' | 'Fijian' | 'Finnish' | 'French' | 'Gabonese' | 'Gambian' | 'Georgian' | 'German' | 'Ghanaian' | 'Greek' | 'Grenadian' | 'Guatemalan' | 'Guinean' | 'Bissaugunean' | 'Guyanese' | 'Haitian' | 'Honduran' | 'Hungarian' | 'Icelandic' | 'Indian' | 'Indonesian' | 'Iranian' | 'Iraqi' | 'Irish' | 'Israeli' | 'Italian' | 'Jamaican' | 'Japanese' | 'Jordanian' | 'Kazakh' | 'Kenyan' | 'Kiribati' | 'Korean' | 'Kuwaiti' | 'Kyrgyz' | 'Lao' | 'Latvian' | 'Lebanese' | 'Basotho' | 'Liberian' | 'Libyan' | 'Liechtenstein' | 'Lithuanian' | 'Luxembourgish' | 'Malagasy' | 'Malawian' | 'Malaysian' | 'Maldivian' | 'Malian' | 'Maltese' | 'Marshallese' | 'Mauritanian' | 'Mauritian' | 'Mexican' | 'Micronesian' | 'Moldovan' | 'Monacan' | 'Mongolian' | 'Montenegrin' | 'Moroccan' | 'Mozambican' | 'Namibian' | 'Nauruan' | 'Nepali' | 'Dutch' | 'Newzealander' | 'Nicaraguan' | 'Nigerien' | 'Nigerian' | 'Norwegian' | 'Omani' | 'Pakistani' | 'Palauan' | 'Panamanian' | 'Papuanewguinean' | 'Paraguayan' | 'Peruvian' | 'Filipino' | 'Polish' | 'Portuguese' | 'Qatari' | 'Romanian' | 'Russian' | 'Rwandan' | 'Kittitian' | 'Saintlucian' | 'Vincentian' | 'Samoan' | 'Sanmarinese' | 'Saotomean' | 'Saudi' | 'Senegalese' | 'Serbian' | 'Seychellois' | 'Sierraleonean' | 'Singaporean' | 'Slovak' | 'Slovenian' | 'Solomonislander' | 'Somali' | 'Southafrican' | 'Spanish' | 'Srilankan' | 'Sudanese' | 'Surinamese' | 'Swazi' | 'Swedish' | 'Swiss' | 'Syrian' | 'Taiwanese' | 'Tajik' | 'Tanzanian' | 'Thai' | 'Togolese' | 'Tongan' | 'Trinidadian' | 'Tunisian' | 'Turkish' | 'Turkmen' | 'Tuvaluan' | 'Ugandan' | 'Ukrainian' | 'Emirati' | 'British' | 'American' | 'Uruguayan' | 'Uzbek' | 'Nivanuatu' | 'Venezuelan' | 'Vietnamese' | 'Yemeni' | 'Zambian' | 'Zimbabwean';

  /**
   * New password for updating credentials (if applicable)
   */
  newPassword?: string;

  /**
   * Tax Identification Number (NIF) of the user
   */
  nif: string;

  /**
   * Password of the user
   */
  password: string;

  /**
   * Phone number of the user
   */
  phoneNumber: string;

  /**
   * Roles assigned to the user
   */
  roles: Array<string>;
}
